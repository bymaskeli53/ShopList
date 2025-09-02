package com.gundogar.shoplist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun ItemRow(item: ShoppingItem, onToggle: (ShoppingItem) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Column(modifier = Modifier.weight(1f).height(80.dp).clickable { onToggle(item) }.background(color = Color.Yellow)) {
            Text(text = item.title, style = MaterialTheme.typography.bodyLarge)
            if (item.amount.isNotBlank()) {
                Text(text = item.amount, style = MaterialTheme.typography.labelSmall)
            }
        }
// overflow menu / edit icon eklenebilir
    }
}