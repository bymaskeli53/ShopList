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

    override fun getAllLists(): Flow<List<ShoppingList>> {
        return queries.getAllLists().asFlow().mapToList(Dispatchers.IO).map { lists ->
            lists.map { list ->
                val items = queries.getItemsForList(list.id).executeAsList().map { item ->
                    ShoppingItemEntity(
                        id = item.id,
                        title = item.title,
                        amount = item.amount
                    )
                }
                ShoppingListEntity(
                    listId = list.id,
                    title = list.title,
                    isCompleted = list.isCompleted == 1L,
                    createdAt = list.createdAt,
                    items = items
                ).toDomain()
            }
        }
    }

    override suspend fun getListById(listId: String): ShoppingList? = withContext(Dispatchers.Default) {
        val list = queries.getListById(listId).executeAsOneOrNull() ?: return@withContext null
        val items = queries.getItemsForList(listId).executeAsList().map { item ->
            ShoppingItemEntity(
                id = item.id,
                title = item.title,
                amount = item.amount
            )
        }
        ShoppingListEntity(
            listId = list.id,
            title = list.title,
            isCompleted = list.isCompleted == 1L,
            createdAt = list.createdAt,
            items = items
        ).toDomain()
    }

    override suspend fun createList(
        listId: String,
        title: String,
        items: List<Pair<String, String>>
    ) = withContext(Dispatchers.IO) {
        queries.transaction {
            val timestamp = Clock.System.now().toEpochMilliseconds()
            // Insert the list
            queries.insertList(
                id = listId,
                title = title,
                isCompleted = 0L,
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

    override suspend fun updateList(
        listId: String,
        title: String,
        items: List<ShoppingItem>
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
                items.forEachIndexed { index, item ->
                    queries.insertListItem(
                        id = uuid4().toString(),
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
            println("Error updating list: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun toggleListCompleted(listId: String, isCompleted: Boolean) = withContext(Dispatchers.IO) {
        queries.updateListCompletionStatus(isCompleted = if (isCompleted) 1L else 0L, id = listId)
    }

    override suspend fun deleteList(listId: String) = withContext(Dispatchers.IO) {
        queries.deleteList(listId)
    }

    override suspend fun restoreList(list: ShoppingList) = withContext(Dispatchers.IO) {
        queries.transaction {
            val timestamp = Clock.System.now().toEpochMilliseconds()
            // Re-insert the list
            queries.insertList(
                id = list.id,
                title = list.title,
                isCompleted = if (list.isCompleted) 1L else 0L,
                createdAt = list.createdAt,
                updatedAt = timestamp
            )

            // Re-insert all items
            list.items.forEachIndexed { index, item ->
                queries.insertListItem(
                    id = item.id,
                    listId = list.id,
                    title = item.title,
                    amount = item.amount,
                    position = index.toLong()
                )
            }
        }
    }
}
