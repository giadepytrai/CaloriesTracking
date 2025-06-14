package com.example.projecthehehe.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.projecthehehe.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, calories: Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.add_food_item)) },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.food_name)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = calories,
                    onValueChange = { calories = it },
                    label = { Text(stringResource(R.string.calories)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val caloriesInt = calories.toIntOrNull() ?: 0
                    if (name.isNotBlank() && caloriesInt > 0) {
                        onConfirm(name, caloriesInt)
                    }
                }
            ) {
                Text(stringResource(R.string.add))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
} 