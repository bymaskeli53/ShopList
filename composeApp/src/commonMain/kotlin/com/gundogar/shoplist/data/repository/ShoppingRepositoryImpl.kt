package com.gundogar.shoplist.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.benasher44.uuid.uuid4
import com.gundogar.shoplist.data.local.DatabaseDriverFactory
import com.gundogar.shoplist.data.model.ShoppingItemEntity
import com.gundogar.shoplist.data.model.ShoppingListEntity
import com.gundogar.shoplist.data.model.toDomain
import com.gundogar.shoplist.data.model.toEntity
import com.gundogar.shoplist.database.ShopListDatabase
import com.gundogar.shoplist.domain.model.ShoppingItem
import com.gundogar.shoplist.domain.model.ShoppingList
import com.gundogar.shoplist.domain.repository.ShoppingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

/**
 * Implementation of ShoppingRepository interface
 * Handles all database operations for shopping lists
 */
class ShoppingRepositoryImpl(databaseDriverFactory: DatabaseDriverFactory) : ShoppingRepository {

    private val database = ShopListDatabase(databaseDriverFactory.createDriver())
    private val queries = database.shoppingItemQueries

    override fun getAllShoppingLists(): Flow<List<ShoppingList>> {
        return queries.getAllLists().asFlow().mapToList(Dispatchers.IO).map { dbLists ->
            dbLists.map { dbList ->
                val dbItems = queries.getItemsForList(dbList.id).executeAsList().map { dbItem ->
                    ShoppingItemEntity(
                        id = dbItem.id,
                        title = dbItem.title,
                        amount = dbItem.amount
                    )
                }
                ShoppingListEntity(
                    listId = dbList.id,
                    title = dbList.title,
                    isCompleted = dbList.isCompleted == 1L,
                    createdAt = dbList.createdAt,
                    items = dbItems
                ).toDomain()
            }
        }
    }

    override suspend fun getShoppingListById(listId: String): ShoppingList? = withContext(Dispatchers.Default) {
        val dbList = queries.getListById(listId).executeAsOneOrNull() ?: return@withContext null
        val dbItems = queries.getItemsForList(listId).executeAsList().map { dbItem ->
            ShoppingItemEntity(
                id = dbItem.id,
                title = dbItem.title,
                amount = dbItem.amount
            )
        }
        ShoppingListEntity(
            listId = dbList.id,
            title = dbList.title,
            isCompleted = dbList.isCompleted == 1L,
            createdAt = dbList.createdAt,
            items = dbItems
        ).toDomain()
    }

    override suspend fun createShoppingList(
        listId: String,
        title: String,
        shoppingItems: List<Pair<String, String>>
    ) = withContext(Dispatchers.IO) {
        queries.transaction {
            val timestamp = Clock.System.now().toEpochMilliseconds()
            // Insert the shopping list
            queries.insertList(
                id = listId,
                title = title,
                isCompleted = 0L,
                createdAt = timestamp,
                updatedAt = timestamp
            )

            // Insert all shopping items
            shoppingItems.forEachIndexed { index, (itemTitle, itemAmount) ->
                queries.insertListItem(
                    id = uuid4().toString(),
                    listId = listId,
                    title = itemTitle,
                    amount = itemAmount,
                    position = index.toLong()
                )
            }
        }
    }

    override suspend fun updateShoppingList(
        listId: String,
        title: String,
        shoppingItems: List<ShoppingItem>
    ) = withContext(Dispatchers.IO) {
        try {
            queries.transaction {
                // Update title
                queries.updateListTitle(
                    title = title,
                    updatedAt = Clock.System.now().toEpochMilliseconds(),
                    id = listId
                )

                // Delete all existing items for this list
                queries.deleteAllItemsForList(listId)

                // Insert updated items with new IDs to avoid conflicts
                shoppingItems.forEachIndexed { index, shoppingItem ->
                    queries.insertListItem(
                        id = uuid4().toString(),
                        listId = listId,
                        title = shoppingItem.title,
                        amount = shoppingItem.amount,
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
            println("Error updating list: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun toggleShoppingListCompletion(listId: String, isCompleted: Boolean) = withContext(Dispatchers.IO) {
        queries.updateListCompletionStatus(isCompleted = if (isCompleted) 1L else 0L, id = listId)
    }

    override suspend fun deleteShoppingList(listId: String) = withContext(Dispatchers.IO) {
        queries.deleteList(listId)
    }

    override suspend fun restoreShoppingList(shoppingList: ShoppingList) = withContext(Dispatchers.IO) {
        queries.transaction {
            val timestamp = Clock.System.now().toEpochMilliseconds()
            // Re-insert the shopping list
            queries.insertList(
                id = shoppingList.id,
                title = shoppingList.title,
                isCompleted = if (shoppingList.isCompleted) 1L else 0L,
                createdAt = shoppingList.createdAt,
                updatedAt = timestamp
            )

            // Re-insert all shopping items
            shoppingList.items.forEachIndexed { index, shoppingItem ->
                queries.insertListItem(
                    id = shoppingItem.id,
                    listId = shoppingList.id,
                    title = shoppingItem.title,
                    amount = shoppingItem.amount,
                    position = index.toLong()
                )
            }
        }
    }
}
