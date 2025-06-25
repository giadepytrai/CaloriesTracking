package com.example.projecthehehe.ui.components

import kotlin.math.roundToInt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.projecthehehe.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, calories: Int, grams: Double) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var caloriesPer100g by remember { mutableStateOf("") }
    var grams by remember { mutableStateOf("") }
      // Calculate total calories based on grams and calories per 100g
    val totalCalories = caloriesPer100g.toDoubleOrNull()?.let { cal100g ->
        grams.toDoubleOrNull()?.let { g ->
            ((cal100g * g) / 100.0).roundToInt()
        }
    } ?: 0
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.add_food_item)) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Food name
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.food_name)) },
                    modifier = Modifier.fillMaxWidth()
                )
                  // Calories per 100g
                OutlinedTextField(
                    value = caloriesPer100g,
                    onValueChange = { newValue ->
                        // Only allow valid decimal numbers
                        if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                            caloriesPer100g = newValue
                        }
                    },
                    label = { Text("Calories per 100g") },
                    suffix = { Text("kcal/100g") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    supportingText = {
                        if (caloriesPer100g.isNotEmpty() && caloriesPer100g.toDoubleOrNull() == null) {
                            Text(
                                text = "Please enter a valid number",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    isError = caloriesPer100g.isNotEmpty() && caloriesPer100g.toDoubleOrNull() == null,
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Grams
                OutlinedTextField(
                    value = grams,
                    onValueChange = { newValue ->
                        // Only allow valid decimal numbers
                        if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                            grams = newValue
                        }
                    },
                    label = { Text("Amount") },
                    suffix = { Text("g") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    supportingText = {
                        if (grams.isNotEmpty() && grams.toDoubleOrNull() == null) {
                            Text(
                                text = "Please enter a valid number",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    isError = grams.isNotEmpty() && grams.toDoubleOrNull() == null,
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Total calories calculation
                if (totalCalories > 0) {
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
                                text = "Total calories:",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "$totalCalories kcal",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val gramsValue = grams.toDoubleOrNull() ?: 0.0
                    if (name.isNotBlank() && totalCalories > 0 && gramsValue > 0) {
                        onConfirm(name, totalCalories, gramsValue)
                    }
                },
                enabled = name.isNotBlank() && totalCalories > 0 && (grams.toDoubleOrNull() ?: 0.0) > 0
            ) {
                Text(stringResource(R.string.add))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }    )
}