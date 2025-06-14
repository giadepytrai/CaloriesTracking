package com.example.projecthehehe.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.projecthehehe.R
import com.example.projecthehehe.data.FoodItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodItemCard(
    foodItem: FoodItem,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = foodItem.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${foodItem.calories} ${stringResource(R.string.unit_kcal)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete))
            }
        }
    }
} 