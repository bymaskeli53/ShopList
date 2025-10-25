package com.gundogar.shoplist.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gundogar.shoplist.domain.model.ShoppingList
import com.gundogar.shoplist.domain.repository.ShoppingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the Shopping List screen
 * Handles business logic for displaying and managing shopping lists
 */
class ShoppingListViewModel(
    private val repository: ShoppingRepository
) : ViewModel() {

    private val _lists = MutableStateFlow<List<ShoppingList>>(emptyList())
    val lists: StateFlow<List<ShoppingList>> = _lists.asStateFlow()

    init {
        loadLists()
    }

    private fun loadLists() {
        viewModelScope.launch {
            repository.getAllLists().collect { lists ->
                _lists.value = lists
            }
        }
    }

    fun toggleListCompleted(list: ShoppingList) {
        viewModelScope.launch {
            repository.toggleListCompleted(list.id, !list.isCompleted)
        }
    }

    fun deleteList(listId: String) {
        viewModelScope.launch {
            repository.deleteList(listId)
        }
    }

    fun restoreList(list: ShoppingList) {
        viewModelScope.launch {
            repository.restoreList(list)
        }
    }
}
