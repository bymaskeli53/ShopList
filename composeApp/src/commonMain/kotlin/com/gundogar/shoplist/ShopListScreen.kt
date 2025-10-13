package com.gundogar.shoplist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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

    // High-contrast color scheme
    val backgroundColor = Color(0xFF000000) // Pure black
    val surfaceColor = Color(0xFF1A1A1A) // Dark gray
    val accentColor = Color(0xFF00E676) // Bright green
    val textPrimary = Color(0xFFFFFFFF) // White text
    val textSecondary = Color(0xFFB0B0B0) // Light gray text

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Alışveriş Listem",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = surfaceColor,
                    titleContentColor = textPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (newText.isNotBlank()) {
                        onAddItem(newText)
                        newText = ""
                    }
                },
                containerColor = accentColor,
                contentColor = Color.Black,
                modifier = Modifier.size(64.dp) // Minimum 48dp touch target
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add item",
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        containerColor = backgroundColor
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
                .background(backgroundColor)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Input field with improved styling
            OutlinedTextField(
                value = newText,
                onValueChange = { newText = it },
                placeholder = {
                    Text(
                        "Yeni madde ekle...",
                        color = textSecondary
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp), // Minimum touch target height
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = textPrimary,
                    unfocusedTextColor = textPrimary,
                    focusedBorderColor = accentColor,
                    unfocusedBorderColor = textSecondary,
                    cursorColor = accentColor,
                    focusedContainerColor = surfaceColor,
                    unfocusedContainerColor = surfaceColor
                ),
                shape = RoundedCornerShape(12.dp),
                textStyle = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Item count indicator
            if (items.isNotEmpty()) {
                Text(
                    text = "${items.size} madde",
                    style = MaterialTheme.typography.labelMedium,
                    color = textSecondary,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            // Shopping list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items) { item ->
                    ItemRow(
                        item = item,
                        onToggle = { onToggleBought(it) },
                        backgroundColor = surfaceColor,
                        textColor = textPrimary,
                        accentColor = accentColor
                    )
                }

                // Bottom padding for FAB
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

data class ShoppingItem(val id: String, val title: String, val amount: String = "", val bought: Boolean = false)