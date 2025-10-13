package com.gundogar.shoplist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp


@Composable
fun ItemRow(
    item: ShoppingItem,
    onToggle: (ShoppingItem) -> Unit,
    backgroundColor: Color,
    textColor: Color,
    accentColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 72.dp) // Minimum touch target
            .clickable { onToggle(item) },
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
            // Item content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
                    .alpha(if (item.bought) 0.5f else 1f)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = textColor,
                    textDecoration = if (item.bought) TextDecoration.LineThrough else null
                )

                if (item.amount.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.amount,
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor.copy(alpha = 0.7f),
                        textDecoration = if (item.bought) TextDecoration.LineThrough else null
                    )
                }
            }

            // Checkbox icon with minimum touch target
            Box(
                modifier = Modifier
                    .size(48.dp), // Minimum 48dp touch target
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (item.bought) Icons.Filled.CheckCircle else Icons.Outlined.Circle,
                    contentDescription = if (item.bought) "Marked as bought" else "Not bought",
                    tint = if (item.bought) accentColor else textColor.copy(alpha = 0.5f),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}