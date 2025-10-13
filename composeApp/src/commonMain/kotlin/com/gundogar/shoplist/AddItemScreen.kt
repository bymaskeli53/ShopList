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

data class ItemEntry(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String = "",
    val amount: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemScreen(
    onNavigateBack: () -> Unit,
    onCreateList: (List<Pair<String, String>>) -> Unit,
    modifier: Modifier = Modifier
) {
    var items by remember { mutableStateOf(listOf(ItemEntry())) }

    // High-contrast color scheme matching ShoppingListScreen
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
                        "Ürün Ekle",
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
                        val itemsToAdd = items
                            .filter { it.title.isNotBlank() }
                            .map { it.title to it.amount }
                        onCreateList(itemsToAdd)
                        onNavigateBack()
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

@Composable
fun ItemEntryCard(
    item: ItemEntry,
    index: Int,
    onTitleChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onDelete: () -> Unit,
    canDelete: Boolean,
    surfaceColor: Color,
    accentColor: Color,
    textPrimary: Color,
    textSecondary: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = surfaceColor
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header with number and delete button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "#${index + 1}",
                    style = MaterialTheme.typography.labelLarge,
                    color = accentColor,
                    fontWeight = FontWeight.Bold
                )

                if (canDelete) {
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Sil",
                            tint = textSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Title input
            OutlinedTextField(
                value = item.title,
                onValueChange = onTitleChange,
                label = { Text("Ürün Adı", color = textSecondary) },
                placeholder = { Text("Örn: Süt", color = textSecondary.copy(alpha = 0.6f)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = textPrimary,
                    unfocusedTextColor = textPrimary,
                    focusedBorderColor = accentColor,
                    unfocusedBorderColor = textSecondary.copy(alpha = 0.5f),
                    focusedLabelColor = accentColor,
                    unfocusedLabelColor = textSecondary,
                    cursorColor = accentColor,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp),
                textStyle = MaterialTheme.typography.bodyLarge,
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Amount input
            OutlinedTextField(
                value = item.amount,
                onValueChange = onAmountChange,
                label = { Text("Miktar", color = textSecondary) },
                placeholder = { Text("Örn: 2 adet", color = textSecondary.copy(alpha = 0.6f)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = textPrimary,
                    unfocusedTextColor = textPrimary,
                    focusedBorderColor = accentColor,
                    unfocusedBorderColor = textSecondary.copy(alpha = 0.5f),
                    focusedLabelColor = accentColor,
                    unfocusedLabelColor = textSecondary,
                    cursorColor = accentColor,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp),
                textStyle = MaterialTheme.typography.bodyLarge,
                singleLine = true
            )
        }
    }
}
