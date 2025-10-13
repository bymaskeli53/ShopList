package com.gundogar.shoplist.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.gundogar.shoplist.database.ShopListDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

data class ShoppingListWithItems(
    val listId: String,
    val bought: Boolean,
    val createdAt: Long,
    val items: List<ShoppingListItemData>
)

data class ShoppingListItemData(
    val id: String,
    val title: String,
    val amount: String
)

class ShoppingRepository(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = ShopListDatabase(databaseDriverFactory.createDriver())
    private val queries = database.shoppingItemQueries

    fun getAllListsWithItems(): Flow<List<ShoppingListWithItems>> {
        return queries.getAllLists().asFlow().mapToList(Dispatchers.Default).map { lists ->
            lists.map { list ->
                val items = queries.getItemsForList(list.id).executeAsList().map { item ->
                    ShoppingListItemData(
                        id = item.id,
                        title = item.title,
                        amount = item.amount
                    )
                }
                ShoppingListWithItems(
                    listId = list.id,
                    bought = list.bought == 1L,
                    createdAt = list.createdAt,
                    items = items
                )
            }
        }
    }

    suspend fun getListWithItems(listId: String): ShoppingListWithItems? = withContext(Dispatchers.Default) {
        val list = queries.getListById(listId).executeAsOneOrNull() ?: return@withContext null
        val items = queries.getItemsForList(listId).executeAsList().map { item ->
            ShoppingListItemData(
                id = item.id,
                title = item.title,
                amount = item.amount
            )
        }
        ShoppingListWithItems(
            listId = list.id,
            bought = list.bought == 1L,
            createdAt = list.createdAt,
            items = items
        )
    }

    suspend fun insertListWithItems(
        listId: String,
        items: List<Pair<String, String>>  // title, amount pairs
    ) = withContext(Dispatchers.Default) {
        queries.transaction {
            val timestamp = System.currentTimeMillis()
            // Insert the list
            queries.insertList(
                id = listId,
                bought = 0L,
                createdAt = timestamp,
                updatedAt = timestamp
            )

            // Insert all items
            items.forEachIndexed { index, (title, amount) ->
                queries.insertListItem(
                    id = java.util.UUID.randomUUID().toString(),
                    listId = listId,
                    title = title,
                    amount = amount,
                    position = index.toLong()
                )
            }
        }
    }

    suspend fun updateListItems(
        listId: String,
        items: List<ShoppingListItemData>
    ) = withContext(Dispatchers.Default) {
        try {
            queries.transaction {
                // Delete all existing items for this list
                queries.deleteAllItemsForList(listId)

                // Insert updated items with new IDs to avoid conflicts
                items.forEachIndexed { index, item ->
                    queries.insertListItem(
                        id = java.util.UUID.randomUUID().toString(),  // Generate new UUID for each item
                        listId = listId,
                        title = item.title,
                        amount = item.amount,
                        position = index.toLong()
                    )
                }

                // Touch the list to trigger Flow update
                queries.touchList(
                    updatedAt = System.currentTimeMillis(),
                    id = listId
                )
            }
        } catch (e: Exception) {
            println("Error updating list items: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

    suspend fun updateListBoughtStatus(listId: String, bought: Boolean) = withContext(Dispatchers.Default) {
        queries.updateListBoughtStatus(bought = if (bought) 1L else 0L, id = listId)
    }

    suspend fun deleteList(listId: String) = withContext(Dispatchers.Default) {
        queries.deleteList(listId)
    }
}
