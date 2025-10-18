package com.gundogar.shoplist.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.gundogar.shoplist.database.ShopListDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import com.benasher44.uuid.uuid4

data class ShoppingListWithItems(
    val listId: String,
    val title: String,
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
        return queries.getAllLists().asFlow().mapToList(Dispatchers.IO).map { lists ->
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
                    title = list.title,
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
            title = list.title,
            bought = list.bought == 1L,
            createdAt = list.createdAt,
            items = items
        )
    }

    suspend fun insertListWithItems(
        listId: String,
        title: String,
        items: List<Pair<String, String>>  // title, amount pairs
    ) = withContext(Dispatchers.IO) {
        queries.transaction {
            val timestamp = Clock.System.now().toEpochMilliseconds()
            // Insert the list
            queries.insertList(
                id = listId,
                title = title,
                bought = 0L,
                createdAt = timestamp,
                updatedAt = timestamp
            )

            // Insert all items
            items.forEachIndexed { index, (title, amount) ->
                queries.insertListItem(
                    id = uuid4().toString(),
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
    ) = withContext(Dispatchers.IO) {
        try {
            queries.transaction {
                // Delete all existing items for this list
                queries.deleteAllItemsForList(listId)

                // Insert updated items with new IDs to avoid conflicts
                items.forEachIndexed { index, item ->
                    queries.insertListItem(
                        id = uuid4().toString(),  // Generate new UUID for each item
                        listId = listId,
                        title = item.title,
                        amount = item.amount,
                        position = index.toLong()
                    )
                }

                // Touch the list to trigger Flow update
                queries.touchList(
                    updatedAt = Clock.System.now().toEpochMilliseconds(),
                    id = listId
                )
            }
        } catch (e: Exception) {
            println("Error updating list items: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

    suspend fun updateListBoughtStatus(listId: String, bought: Boolean) = withContext(Dispatchers.IO) {
        queries.updateListBoughtStatus(bought = if (bought) 1L else 0L, id = listId)
    }

    suspend fun updateListTitle(listId: String, title: String) = withContext(Dispatchers.IO) {
        queries.updateListTitle(
            title = title,
            updatedAt = Clock.System.now().toEpochMilliseconds(),
            id = listId
        )
    }

    suspend fun deleteList(listId: String) = withContext(Dispatchers.IO) {
        queries.deleteList(listId)
    }

    suspend fun restoreList(
        listId: String,
        title: String,
        bought: Boolean,
        createdAt: Long,
        items: List<ShoppingListItemData>
    ) = withContext(Dispatchers.IO) {
        queries.transaction {
            val timestamp = Clock.System.now().toEpochMilliseconds()
            // Re-insert the list
            queries.insertList(
                id = listId,
                title = title,
                bought = if (bought) 1L else 0L,
                createdAt = createdAt,  // Use original creation time
                updatedAt = timestamp
            )

            // Re-insert all items
            items.forEachIndexed { index, item ->
                queries.insertListItem(
                    id = item.id,  // Use original item IDs
                    listId = listId,
                    title = item.title,
                    amount = item.amount,
                    position = index.toLong()
                )
            }
        }
    }
}
