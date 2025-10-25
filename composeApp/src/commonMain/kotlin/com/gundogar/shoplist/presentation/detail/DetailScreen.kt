package com.gundogar.shoplist.presentation.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gundogar.shoplist.domain.model.ShoppingItem
import com.gundogar.shoplist.domain.model.ShoppingItemInput
import com.gundogar.shoplist.domain.model.ShoppingList
import com.gundogar.shoplist.presentation.add.ItemEntryCard
import com.gundogar.shoplist.util.tts.TextToSpeechManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    list: ShoppingList,
    onNavigateBack: () -> Unit,
    onSave: suspend (String, String, List<ShoppingItem>) -> Unit,
    modifier: Modifier = Modifier
) {
    // Use remember with key to reset when list changes
    var items by remember(list.id, list.items) {
        mutableStateOf(list.items.map {
            ShoppingItemInput(id = it.id, title = it.title, amount = it.amount)
        })
    }

    val initialItem = remember { ShoppingItemInput() }

    var listTitle by remember(list.id) { mutableStateOf(list.title) }

    var visibleItems by remember { mutableStateOf(mapOf(initialItem.id to true)) }


    val scope = rememberCoroutineScope()
    val ttsManager = remember { TextToSpeechManager() }

    // Clean up TTS when leaving the screen
    DisposableEffect(Unit) {
        onDispose {
            ttsManager.shutdown()
        }
    }

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
                    // TTS button
                    IconButton(
                        onClick = {
                            val spokenText = buildString {
                                append("Alışveriş listenizde. ")
                                items.filter { it.title.isNotBlank() }
                                    .forEachIndexed { index, item ->
                                        if (item.amount.isNotBlank()) {
                                            append("${item.amount} ${item.title}")
                                        } else {
                                            append(item.title)
                                        }
                                        if (index < items.size - 1) {
                                            append(", ")
                                        }
                                    }
                                append("Bulunmaktadır")
                            }
                            ttsManager.speak(spokenText)
                        },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            Icons.Default.VolumeUp,
                            contentDescription = "Listeyi oku",
                            tint = accentColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Add new item button
                    IconButton(
                        onClick = {
                            val newItem = ShoppingItemInput()
                            items = items + newItem

                            visibleItems = visibleItems + (newItem.id to false)

                            scope.launch {
                                delay(50)
                                visibleItems = visibleItems + (newItem.id to true)
                            }
                        },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Yeni ürün ekle",
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
            var isPressed by remember { mutableStateOf(false) }

            val size by animateDpAsState(
                targetValue = if (isPressed) 76.dp else 64.dp
            )
            val hasValidItems = items.any { it.title.isNotBlank() }
            if (hasValidItems) {
                FloatingActionButton(
                    onClick = {
                        isPressed = !isPressed
                        scope.launch {
                            val updatedItems = items
                                .filter { it.title.isNotBlank() }
                                .map {
                                    ShoppingItem(
                                        id = it.id,
                                        title = it.title,
                                        amount = it.amount
                                    )
                                }
                            // Wait for the save to complete
                            onSave(list.id, listTitle, updatedItems)
                            // Navigate back after saving completes
                            onNavigateBack()
                        }
                    },
                    containerColor = accentColor,
                    contentColor = Color.Black,
                    modifier = Modifier.size(size)
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
                    text = "${items.size} ürün",
                    style = MaterialTheme.typography.labelLarge,
                    color = textPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Sağ üstteki + ile yeni ürün ekleyin",
                    style = MaterialTheme.typography.bodySmall,
                    color = textSecondary
                )
            }

            // List Title Input
            OutlinedTextField(
                value = listTitle,
                onValueChange = { listTitle = it },
                label = { Text("Liste Başlığı", color = textSecondary) },
                placeholder = {
                    Text(
                        "Örn: Haftalık Alışveriş",
                        color = textSecondary.copy(alpha = 0.6f)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = textPrimary,
                    unfocusedTextColor = textPrimary,
                    focusedBorderColor = accentColor,
                    unfocusedBorderColor = textSecondary.copy(alpha = 0.5f),
                    focusedLabelColor = accentColor,
                    unfocusedLabelColor = textSecondary,
                    cursorColor = accentColor,
                    focusedContainerColor = surfaceColor,
                    unfocusedContainerColor = surfaceColor
                ),
                shape = RoundedCornerShape(12.dp),
                textStyle = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))


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
                    AnimatedVisibility(
                        visible = visibleItems[item.id] ?: true,
                        enter = fadeIn(
                            animationSpec = tween(300)) + slideInVertically(
                            initialOffsetY = { -it / 2 },
                            animationSpec = tween(300)
                            ),
                        exit = fadeOut(
                            animationSpec = tween(300)) + slideOutVertically(
                                targetOffsetY = { it / 2 },
                            animationSpec = tween(300)
                        )
                    ) {
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
                                    visibleItems = visibleItems + (item.id to false)

                                    scope.launch {
                                        delay(300)
                                        items = items.filterIndexed { i, _ -> i != index }

                                    }
                                }
                            },
                            canDelete = items.size > 1,
                            surfaceColor = surfaceColor,
                            accentColor = accentColor,
                            textPrimary = textPrimary,
                            textSecondary = textSecondary
                        )
                    }

                }

                // Bottom padding for FAB
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}
