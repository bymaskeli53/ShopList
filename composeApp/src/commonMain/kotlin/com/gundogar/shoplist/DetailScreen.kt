package com.gundogar.shoplist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    list: ShoppingListUI,
    onNavigateBack: () -> Unit,
    onSave: suspend (String, List<ShoppingListItemUI>) -> Unit,
    modifier: Modifier = Modifier
) {
    // Use remember with key to reset when list changes
    var items by remember(list.id, list.items) {
        mutableStateOf(list.items.map {
            ItemEntry(id = it.id, title = it.title, amount = it.amount)
        })
    }

    val scope = rememberCoroutineScope()

    // High-contrast color scheme matching AddItemScreen
    val backgroundColor = Color(0xFF000000)
    val surfaceColor = Color(0xFF1A1A1A)
    val accentColor = Color(0xFF00E676)
    val textPrimary = Color(0xFFFFFFFF)
    val textSecondary = Color(0xFFB0B0B0)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Listeyi Düzenle",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Geri",
                            tint = textPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                actions = {
                    // Add new item button
                    IconButton(
                        onClick = { items = items + ItemEntry() },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Yeni satır ekle",
                            tint = accentColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = surfaceColor,
                    titleContentColor = textPrimary
                )
            )
        },
        floatingActionButton = {
            val hasValidItems = items.any { it.title.isNotBlank() }
            if (hasValidItems) {
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            val updatedItems = items
                                .filter { it.title.isNotBlank() }
                                .map {
                                    ShoppingListItemUI(
                                        id = it.id,
                                        title = it.title,
                                        amount = it.amount
                                    )
                                }
                            // Wait for the save to complete
                            onSave(list.id, updatedItems)
                            // Navigate back after saving completes
                            onNavigateBack()
                        }
                    },
                    containerColor = accentColor,
                    contentColor = Color.Black,
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "Kaydet",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        },
        containerColor = backgroundColor
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            // Header with instruction
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(surfaceColor)
                    .padding(20.dp)
            ) {
                Text(
                    text = "${items.size} satır",
                    style = MaterialTheme.typography.labelLarge,
                    color = textPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Sağ üstteki + ile yeni satır ekleyin",
                    style = MaterialTheme.typography.bodySmall,
                    color = textSecondary
                )
            }

            // Item list
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                itemsIndexed(
                    items = items,
                    key = { _, item -> item.id }
                ) { index, item ->
                    ItemEntryCard(
                        item = item,
                        index = index,
                        onTitleChange = { newTitle ->
                            items = items.toMutableList().apply {
                                this[index] = this[index].copy(title = newTitle)
                            }
                        },
                        onAmountChange = { newAmount ->
                            items = items.toMutableList().apply {
                                this[index] = this[index].copy(amount = newAmount)
                            }
                        },
                        onDelete = {
                            if (items.size > 1) {
                                items = items.filterIndexed { i, _ -> i != index }
                            }
                        },
                        canDelete = items.size > 1,
                        surfaceColor = surfaceColor,
                        accentColor = accentColor,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary
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
