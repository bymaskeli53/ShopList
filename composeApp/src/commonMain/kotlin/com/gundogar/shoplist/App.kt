package com.gundogar.shoplist

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gundogar.shoplist.data.DatabaseDriverFactory
import com.gundogar.shoplist.data.ShoppingRepository
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(databaseDriverFactory: DatabaseDriverFactory) {
    MaterialTheme {
        val navController = rememberNavController()
        val repository = remember { ShoppingRepository(databaseDriverFactory) }
        val viewModel: ShoppingViewModel = viewModel { ShoppingViewModel(repository) }
        val lists by viewModel.lists.collectAsState()

        NavHost(
            navController = navController,
            startDestination = "shopping_list"
        ) {
            composable("shopping_list") {
                ShoppingListScreen(
                    lists = lists,
                    onToggleBought = { list -> viewModel.toggleListBought(list) },
                    onListClick = { list -> navController.navigate("detail/${list.id}") },
                    onNavigateToAdd = { navController.navigate("add_item") }
                )
            }

            composable("add_item") {
                AddItemScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onCreateList = { items -> viewModel.createList(items) }
                )
            }

            composable(
                route = "detail/{listId}"
            ) { backStackEntry ->
                val listId = backStackEntry.arguments?.getString("listId")
                val list = lists.find { it.id == listId }

                if (list != null) {
                    DetailScreen(
                        list = list,
                        onNavigateBack = { navController.popBackStack() },
                        onSave = { id: String, items: List<ShoppingListItemUI> ->
                            viewModel.updateListItems(id, items)
                        }
                    )
                }
            }
        }
    }
}