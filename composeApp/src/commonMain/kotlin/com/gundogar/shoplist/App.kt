package com.gundogar.shoplist

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gundogar.shoplist.data.DatabaseDriverFactory
import com.gundogar.shoplist.data.ShoppingRepository
import com.gundogar.shoplist.ui.theme.ShopListTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(databaseDriverFactory: DatabaseDriverFactory) {
    ShopListTheme {
        val navController = rememberNavController()
        val repository = remember { ShoppingRepository(databaseDriverFactory) }
        val viewModel: ShoppingViewModel = viewModel { ShoppingViewModel(repository) }
        val lists by viewModel.lists.collectAsState()

        NavHost(
            navController = navController,
            startDestination = "shopping_list",
        ) {
            composable("shopping_list") {
                ShoppingListScreen(
                    lists = lists,
                    onToggleCompleted = { list -> viewModel.toggleListCompleted(list) },
                    onListClick = { list -> navController.navigate("detail/${list.id}") },
                    onNavigateToAdd = { navController.navigate("add_item") },
                    onDeleteList = { list -> viewModel.deleteList(list.id) },
                    onRestoreList = { list -> viewModel.restoreList(list) }
                )
            }

            composable("add_item") {
                AddItemScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onCreateList = { title, items -> viewModel.createList(items, title) }
                )
            }

            composable(
                route = "detail/{listId}"
            ) { backStackEntry ->
                // Extract listId using Navigation 2.9.0 API
                val listId = backStackEntry.savedStateHandle.get<String>("listId")

                // Re-find the list from the reactive StateFlow on every recomposition
                val list = lists.find { it.id == listId }

                if (list != null) {
                    DetailScreen(
                        list = list,
                        onNavigateBack = { navController.popBackStack() },
                        onSave = { id: String, title: String, items: List<ShoppingListItemUI> ->
                            // This is now a suspend function that waits for update to complete
                            viewModel.updateList(id, title, items)
                        }
                    )
                }
            }
        }
    }
}