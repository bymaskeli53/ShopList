package com.gundogar.shoplist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gundogar.shoplist.data.ShoppingRepository
import com.gundogar.shoplist.data.ShoppingListItemData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.benasher44.uuid.uuid4

class ShoppingViewModel(
    private val repository: ShoppingRepository
) : ViewModel() {

    private val _lists = MutableStateFlow<List<ShoppingListUI>>(emptyList())
    val lists: StateFlow<List<ShoppingListUI>> = _lists.asStateFlow()

    init {
        loadLists()
    }

    private fun loadLists() {
        viewModelScope.launch {
            repository.getAllListsWithItems().collect { dbLists ->
                _lists.value = dbLists.map { dbList ->
                    ShoppingListUI(
                        id = dbList.listId,
                        title = dbList.title,
                        items = dbList.items.map { item ->
                            ShoppingListItemUI(
                                id = item.id,
                                title = item.title,
                                amount = item.amount
                            )
                        },
                        isCompleted = dbList.bought,
                        createdAt = dbList.createdAt
                    )
                }
            }
        }
    }

    fun createList(items: List<Pair<String, String>>, title: String = "") {
        if (items.isEmpty()) return

        viewModelScope.launch {
            val listId = uuid4().toString()
            repository.insertListWithItems(listId, title, items)
        }
    }

    fun updateListItems(listId: String, items: List<ShoppingListItemUI>) {
        viewModelScope.launch {
            val dataItems = items.map { item ->
                ShoppingListItemData(
                    id = item.id,
                    title = item.title,
                    amount = item.amount
                )
            }
            repository.updateListItems(listId, dataItems)
        }
    }

    suspend fun updateList(listId: String, listTitle: String, items: List<ShoppingListItemUI>) {
        // Update title
        repository.updateListTitle(listId, listTitle)

        // Update items
        val dataItems = items.map { item ->
            ShoppingListItemData(
                id = item.id,
                title = item.title,
                amount = item.amount
            )
        }
        repository.updateListItems(listId, dataItems)
    }

    fun toggleListCompleted(list: ShoppingListUI) {
        viewModelScope.launch {
            repository.updateListBoughtStatus(list.id, !list.isCompleted)
        }
    }

    fun deleteList(listId: String) {
        viewModelScope.launch {
            repository.deleteList(listId)
        }
    }

    fun restoreList(list: ShoppingListUI) {
        viewModelScope.launch {
            val items = list.items.map { item ->
                ShoppingListItemData(
                    id = item.id,
                    title = item.title,
                    amount = item.amount
                )
            }
            repository.restoreList(
                listId = list.id,
                title = list.title,
                bought = list.isCompleted,
                createdAt = list.createdAt,
                items = items
            )
        }
    }
}
