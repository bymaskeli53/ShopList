package com.gundogar.shoplist.presentation.detail

import androidx.lifecycle.ViewModel
import com.gundogar.shoplist.domain.model.ShoppingItem
import com.gundogar.shoplist.domain.repository.ShoppingRepository

/**
 * ViewModel for the Detail screen
 * Handles business logic for editing shopping lists
 */
class DetailViewModel(
    private val repository: ShoppingRepository
) : ViewModel() {

    suspend fun updateList(listId: String, title: String, items: List<ShoppingItem>) {
        repository.updateList(listId, title, items)
    }
}
