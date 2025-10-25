package com.gundogar.shoplist.domain.model

/**
 * Domain model representing a single item in a shopping list
 */
data class ShoppingItem(
    val id: String,
    val title: String,
    val amount: String = ""
)
