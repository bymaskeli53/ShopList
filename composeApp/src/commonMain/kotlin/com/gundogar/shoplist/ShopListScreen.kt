package com.gundogar.shoplist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gundogar.shoplist.tts.TextToSpeechManager
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(
    lists: List<ShoppingListUI>,
    onToggleBought: (ShoppingListUI) -> Unit = {},
    onListClick: (ShoppingListUI) -> Unit = {},
    onNavigateToAdd: () -> Unit = {},
    onDeleteList: (ShoppingListUI) -> Unit = {},
    onRestoreList: (ShoppingListUI) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val ttsManager = remember { TextToSpeechManager() }

    // Track recently deleted list for undo
    var deletedList by remember { mutableStateOf<ShoppingListUI?>(null) }

    // Clean up TTS when leaving the screen
    DisposableEffect(Unit) {
        onDispose {
            ttsManager.shutdown()
        }
    }

    // High-contrast color scheme
    val backgroundColor = Color(0xFF000000) // Pure black
    val surfaceColor = Color(0xFF1A1A1A) // Dark gray
    val accentColor = Color(0xFF00E676) // Bright green
    val textPrimary = Color(0xFFFFFFFF) // White text
    val textSecondary = Color(0xFFB0B0B0) // Light gray text
    val deleteColor = Color(0xFFFF5252) // Red for delete

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
            var isPressed by remember { mutableStateOf(false) }

            val size by animateDpAsState(
                targetValue = if (isPressed) 76.dp else 64.dp
            )

            FloatingActionButton(
                onClick = {
                    isPressed = !isPressed
                    onNavigateToAdd.invoke()
                },
                containerColor = accentColor,
                contentColor = Color.Black,
                modifier = Modifier.size(size) // Minimum 48dp touch target
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add item",
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        snackbarHost = {
            AnimatedSnackbarHost(hostState = snackbarHostState)
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
            // List count indicator
            if (lists.isNotEmpty()) {
                Text(
                    text = "${lists.size} adet listeniz var",
                    style = MaterialTheme.typography.labelMedium,
                    color = textSecondary,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            } else {
                // Empty state
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Henüz liste eklenmedi",
                        style = MaterialTheme.typography.bodyLarge,
                        color = textSecondary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Sağ alttaki + butonuna tıklayarak başlayın",
                        style = MaterialTheme.typography.bodyMedium,
                        color = textSecondary.copy(alpha = 0.7f)
                    )
                }
            }

            // Shopping lists
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = lists,
                    key = { list -> list.id }
                ) { list ->
//                    AnimatedVisibility(
//                        visible = true,
//                        enter = fadeIn(animationSpec = tween(300)) + slideInVertically(initialOffsetY = {it / 2}),
//                        exit = fadeOut(animationSpec = tween(300)) + slideOutVertically(targetOffsetY = {-it / 2})
//
//                    ) {


                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { dismissValue ->
                            if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                                // Store the deleted list
                                deletedList = list

                                // Delete the list
                                onDeleteList(list)

                                // Show snackbar with undo option
                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = "Liste silindi",
                                        actionLabel = "Geri Al",
                                        duration = SnackbarDuration.Short
                                    )

                                    if (result == SnackbarResult.ActionPerformed) {
                                        // Undo: restore the list
                                        deletedList?.let { restoredList ->
                                            onRestoreList(restoredList)
                                        }
                                    }
                                    deletedList = null
                                }
                                true
                            } else {
                                false
                            }
                        },
                        positionalThreshold = { distance -> distance * 0.12f }

                    )

                    val backgroundColor by animateColorAsState(
                        targetValue = when (dismissState.targetValue) {
                            SwipeToDismissBoxValue.EndToStart -> deleteColor
                            else -> Color.Transparent
                        },
                        animationSpec = tween(300)
                    )

                    SwipeToDismissBox(
                        state = dismissState,
                        backgroundContent = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(backgroundColor, RoundedCornerShape(16.dp))
                                    .padding(horizontal = 20.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Sil",
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        },
                        enableDismissFromStartToEnd = false,
                        enableDismissFromEndToStart = true
                    ) {
                        ListRow(
                            list = list,
                            onClick = { onListClick(it) },
                            onToggle = { onToggleBought(it) },
                            onSpeak = { list ->
                                // Create a spoken text from the list items
                                val spokenText = buildString {
                                    append("Alışveriş listenizde. ")
                                    list.items.forEachIndexed { index, item ->
                                        if (item.amount.isNotBlank()) {
                                            append("${item.amount} ${item.title}")
                                        } else {
                                            append(item.title)
                                        }
                                        if (index < list.items.size - 1) {
                                            append(", ")
                                        }
                                    }
                                    append("Bulunmaktadır")
                                }
                                ttsManager.speak(spokenText)
                            },
                            backgroundColor = surfaceColor,
                            textColor = textPrimary,
                            accentColor = accentColor
                        )
                    }
                }
           //  }

                // Bottom padding for FAB
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

// UI Models
data class ShoppingListUI(
    val id: String,
    val title: String,
    val items: List<ShoppingListItemUI>,
    val bought: Boolean = false,
    val createdAt: Long
)

data class ShoppingListItemUI(
    val id: String,
    val title: String,
    val amount: String = ""
)

// Already using SnackbarHost, but can enhance with custom animation
@Composable
fun AnimatedSnackbarHost(hostState: SnackbarHostState) {
    SnackbarHost(
        hostState = hostState,
        snackbar = { data ->
            var visible by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                visible = true
            }

            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                Snackbar(containerColor = Color(0xFF33B186),snackbarData = data)
            }
        }
    )
}