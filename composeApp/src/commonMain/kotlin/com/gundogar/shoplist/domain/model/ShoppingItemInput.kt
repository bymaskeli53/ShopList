package com.gundogar.shoplist.domain.model

import com.benasher44.uuid.uuid4

/**
 * Form input state for creating/editing shopping items
 * Used in AddItemScreen and DetailScreen
 */
data class ShoppingItemInput(
    val id: String = uuid4().toString(),
    val title: String = "",
    val amount: String = ""
)
