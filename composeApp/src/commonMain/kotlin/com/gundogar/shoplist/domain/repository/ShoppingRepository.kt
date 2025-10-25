package com.gundogar.shoplist.domain.repository

import com.gundogar.shoplist.domain.model.ShoppingItem
import com.gundogar.shoplist.domain.model.ShoppingList
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for shopping list operations
 * This follows the Dependency Inversion Principle - domain layer defines the contract
 */
interface ShoppingRepository {

    /**
     * Get all shopping lists with their items as a Flow
     */
    fun getAllLists(): Flow<List<ShoppingList>>

    /**
     * Get a single list by ID
     */
    suspend fun getListById(listId: String): ShoppingList?

    /**
     * Create a new shopping list with items
     */
    suspend fun createList(
        listId: String,
        title: String,
        items: List<Pair<String, String>> // title, amount pairs
    )

    /**
     * Update an existing list (title and items)
     */
    suspend fun updateList(
        listId: String,
        title: String,
        items: List<ShoppingItem>
    )

    /**
     * Toggle the completed status of a list
     */
    suspend fun toggleListCompleted(listId: String, isCompleted: Boolean)

    /**
     * Delete a list
     */
    suspend fun deleteList(listId: String)

    /**
     * Restore a deleted list (for undo functionality)
     */
    suspend fun restoreList(list: ShoppingList)
}
