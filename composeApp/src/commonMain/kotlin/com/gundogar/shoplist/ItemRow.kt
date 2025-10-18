package com.gundogar.shoplist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp


@Composable
fun ListRow(
    list: ShoppingListUI,
    onClick: (ShoppingListUI) -> Unit,
    onToggle: (ShoppingListUI) -> Unit,
    onSpeak: (ShoppingListUI) -> Unit = {},
    backgroundColor: Color,
    textColor: Color,
    accentColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 72.dp) // Minimum touch target
            .clickable { onClick(list) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 8.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // List content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
                    .alpha(if (list.bought) 0.5f else 1f)
            ) {
                // Show list title if available, otherwise show items
                if (list.title.isNotBlank()) {
                    Text(
                        text = list.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        textDecoration = if (list.bought) TextDecoration.LineThrough else null,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Show first few items as preview
                    val itemsPreview = list.items.take(2).joinToString(", ") { item ->
                        if (item.amount.isNotBlank()) {
                            "${item.amount} ${item.title}"
                        } else {
                            item.title
                        }
                    } + if (list.items.size > 2) "..." else ""

                    Text(
                        text = itemsPreview,
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor.copy(alpha = 0.7f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                } else {
                    // Fallback: Show items as comma-separated list
                    val itemsText = list.items.joinToString(", ") { item ->
                        if (item.amount.isNotBlank()) {
                            "${item.amount} ${item.title}"
                        } else {
                            item.title
                        }
                    }

                    Text(
                        text = itemsText,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = textColor,
                        textDecoration = if (list.bought) TextDecoration.LineThrough else null,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${list.items.size} ürün",
                    style = MaterialTheme.typography.bodySmall,
                    color = textColor.copy(alpha = 0.6f)
                )
            }

            // Action buttons row
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // TTS button
                IconButton(
                    onClick = { onSpeak(list) },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.VolumeUp,
                        contentDescription = "Listeyi oku",
                        tint = accentColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Checkbox icon with minimum touch target
                Box(
                    modifier = Modifier
                        .size(48.dp) // Minimum 48dp touch target
                        .clickable(onClick = { onToggle(list) }),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (list.bought) Icons.Filled.CheckCircle else Icons.Outlined.Circle,
                        contentDescription = if (list.bought) "Alındı olarak işaretle" else "Alınmadı",
                        tint = if (list.bought) accentColor else textColor.copy(alpha = 0.5f),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}