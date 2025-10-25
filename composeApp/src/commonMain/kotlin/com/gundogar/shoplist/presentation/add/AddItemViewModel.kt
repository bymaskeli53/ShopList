package com.gundogar.shoplist.presentation.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.benasher44.uuid.uuid4
import com.gundogar.shoplist.domain.repository.ShoppingRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for the Add Item screen
 * Handles business logic for creating new shopping lists
 */
class AddItemViewModel(
    private val repository: ShoppingRepository
) : ViewModel() {

    fun createList(title: String, items: List<Pair<String, String>>) {
        if (items.isEmpty()) return

        viewModelScope.launch {
            val listId = uuid4().toString()
            repository.createList(listId, title, items)
        }
    }
}
