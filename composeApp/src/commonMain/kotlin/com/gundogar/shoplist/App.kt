package com.gundogar.shoplist

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gundogar.shoplist.data.local.DatabaseDriverFactory
import com.gundogar.shoplist.data.repository.ShoppingRepositoryImpl
import com.gundogar.shoplist.domain.repository.ShoppingRepository
import com.gundogar.shoplist.presentation.add.AddItemScreen
import com.gundogar.shoplist.presentation.add.AddItemViewModel
import com.gundogar.shoplist.presentation.detail.DetailScreen
import com.gundogar.shoplist.presentation.detail.DetailViewModel
import com.gundogar.shoplist.presentation.list.ShoppingListScreen
import com.gundogar.shoplist.presentation.list.ShoppingListViewModel
import com.gundogar.shoplist.presentation.theme.ShopListTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(databaseDriverFactory: DatabaseDriverFactory) {
    ShopListTheme {
        val navController = rememberNavController()

        // Initialize repository
        val repository: ShoppingRepository = remember { ShoppingRepositoryImpl(databaseDriverFactory) }

        // Initialize ViewModels
        val listViewModel: ShoppingListViewModel = koinViewModel()
        val addViewModel: AddItemViewModel = koinViewModel()
        val detailViewModel: DetailViewModel = koinViewModel()

        // Observe lists from ViewModel
        val lists by listViewModel.lists.collectAsState()

        NavHost(
            navController = navController,
            startDestination = "shopping_list",
        ) {
            composable("shopping_list") {
                ShoppingListScreen(
                    lists = lists,
                    onToggleCompleted = { list -> listViewModel.toggleListCompleted(list) },
                    onListClick = { list -> navController.navigate("detail/${list.id}") },
                    onNavigateToAdd = { navController.navigate("add_item") },
                    onDeleteList = { list -> listViewModel.deleteList(list.id) },
                    onRestoreList = { list -> listViewModel.restoreList(list) }
                )
            }

            composable("add_item") {
                AddItemScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onCreateList = { title, items ->
                        addViewModel.createList(title, items)
                    }
                )
            }

            composable(
                route = "detail/{listId}"
            ) { backStackEntry ->
                val listId = backStackEntry.savedStateHandle.get<String>("listId")
                val list = lists.find { it.id == listId }

                if (list != null) {
                    DetailScreen(
                        list = list,
                        onNavigateBack = { navController.popBackStack() },
                        onSave = { id, title, items ->
                            detailViewModel.updateList(id, title, items)
                        }
                    )
                }
            }
        }
    }
}
