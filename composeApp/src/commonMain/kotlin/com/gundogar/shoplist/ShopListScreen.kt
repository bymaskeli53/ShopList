package com.gundogar.shoplist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(
    items: List<ShoppingItem>,
    onToggleBought: (ShoppingItem) -> Unit = {},
    onAddItem: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var newText by remember { mutableStateOf("") }



    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Alışveriş Listem") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { if (newText.isNotBlank()) { onAddItem(newText); newText = "" } }) {
                Icon(Icons.Default.Add, contentDescription = "Add")

            }
        }
    ) { padding ->
        Column(modifier = modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            OutlinedTextField(
                value = newText,
                onValueChange = { newText = it },
                placeholder = { Text("Yeni madde ekle...") },
                modifier = Modifier.fillMaxWidth()
            )


            Spacer(modifier = Modifier.height(12.dp))


            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(items) { item ->
                    ItemRow(item = item, onToggle = { onToggleBought(it) })
                }
            }
        }
    }
}

data class ShoppingItem(val id: String, val title: String, val amount: String = "", val bought: Boolean = false)