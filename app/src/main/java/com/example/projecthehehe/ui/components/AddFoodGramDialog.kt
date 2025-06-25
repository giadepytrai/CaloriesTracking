package com.example.projecthehehe.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.projecthehehe.data.api.FoodDisplayItem
import kotlin.math.roundToInt

@Composable
fun AddFoodGramDialog(
    foodItem: FoodDisplayItem,
    onDismiss: () -> Unit,
    onConfirm: (grams: Double, adjustedCalories: Int) -> Unit
) {
    var gramsInput by remember { mutableStateOf("") }
    val originalServingSize = foodItem.servingSize ?: 100.0
    val originalCalories = foodItem.calories ?: 0
    
    // Calculate adjusted calories based on grams input
    val adjustedCalories = gramsInput.toDoubleOrNull()?.let { grams ->
        ((originalCalories * grams) / originalServingSize).roundToInt()
    } ?: originalCalories

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Add ${foodItem.title}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Food info
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = "Original serving: ${originalServingSize.toInt()}g = $originalCalories kcal",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Calories per 100g: ${((originalCalories * 100) / originalServingSize).roundToInt()} kcal",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                  // Grams input
                OutlinedTextField(
                    value = gramsInput,
                    onValueChange = { newValue ->
                        // Only allow valid decimal numbers
                        if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                            gramsInput = newValue
                        }
                    },
                    label = { Text("Enter grams") },
                    suffix = { Text("g") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    supportingText = {
                        if (gramsInput.isNotEmpty() && gramsInput.toDoubleOrNull() == null) {
                            Text(
                                text = "Please enter a valid number",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    isError = gramsInput.isNotEmpty() && gramsInput.toDoubleOrNull() == null,
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Calculated calories
                if (gramsInput.isNotEmpty() && gramsInput.toDoubleOrNull() != null) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${gramsInput}g will add:",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "$adjustedCalories kcal",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val grams = gramsInput.toDoubleOrNull()
                    if (grams != null && grams > 0) {
                        onConfirm(grams, adjustedCalories)
                    }
                },
                enabled = gramsInput.toDoubleOrNull()?.let { it > 0 } == true
            ) {
                Text("Add to Tracker")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
