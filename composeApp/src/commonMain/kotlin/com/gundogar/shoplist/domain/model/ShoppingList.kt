package com.gundogar.shoplist.domain.model

/**
 * Domain model representing a shopping list
 * This is the UI/Domain layer model, independent of data layer implementation
 */
data class ShoppingList(
    val id: String,
    val title: String,
    val items: List<ShoppingItem>,
    val isCompleted: Boolean = false,
    val createdAt: Long
)
