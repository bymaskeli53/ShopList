package com.gundogar.shoplist.data.model

import com.gundogar.shoplist.domain.model.ShoppingItem
import com.gundogar.shoplist.domain.model.ShoppingList

/**
 * Data layer entity representing a shopping list with its items
 * This is used internally in the data layer
 */
data class ShoppingListEntity(
    val listId: String,
    val title: String,
    val bought: Boolean,
    val createdAt: Long,
    val items: List<ShoppingItemEntity>
)

/**
 * Data layer entity representing a shopping item
 */
data class ShoppingItemEntity(
    val id: String,
    val title: String,
    val amount: String
)

/**
 * Mapper extension functions to convert between data and domain layers
 */
fun ShoppingListEntity.toDomain(): ShoppingList {
    return ShoppingList(
        id = listId,
        title = title,
        items = items.map { it.toDomain() },
        isCompleted = bought,
        createdAt = createdAt
    )
}

fun ShoppingItemEntity.toDomain(): ShoppingItem {
    return ShoppingItem(
        id = id,
        title = title,
        amount = amount
    )
}

fun ShoppingItem.toEntity(): ShoppingItemEntity {
    return ShoppingItemEntity(
        id = id,
        title = title,
        amount = amount
    )
}

fun ShoppingList.toEntity(): ShoppingListEntity {
    return ShoppingListEntity(
        listId = id,
        title = title,
        bought = isCompleted,
        createdAt = createdAt,
        items = items.map { it.toEntity() }
    )
}
