package com.example.projecthehehe.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecthehehe.R
import com.example.projecthehehe.ui.components.*
import com.example.projecthehehe.viewmodel.CalorieTrackerViewModel

@Composable
fun CalorieTrackerScreen(
    viewModel: CalorieTrackerViewModel = viewModel()
) {    var showAddDialog by remember { mutableStateOf(false) }
    val foodItems by viewModel.foodItems.collectAsState()
    val dailyCalorieNeeds by viewModel.dailyCalorieNeeds.collectAsState()
    
    // Calculate total calories consumed today
    val totalCaloriesToday = foodItems.sumOf { it.calories }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {        Text(
            text = stringResource(R.string.calorie_tracker),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        dailyCalorieNeeds?.let { needs ->
            CalorieNeedsCard(
                dailyCalorieNeeds = needs,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }        // Today's Calories Consumed Card
        TodayCaloriesCard(
            totalCalories = totalCaloriesToday,
            dailyGoal = dailyCalorieNeeds?.toInt() ?: 2000,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = stringResource(R.string.today_food),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(foodItems) { foodItem ->
                FoodItemCard(
                    foodItem = foodItem,
                    onDeleteClick = { viewModel.deleteFoodItem(foodItem) }
                )
            }
        }

        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.End)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_food))
        }
    };    if (showAddDialog) {
        AddFoodDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, calories, grams ->
                // Update name to include grams information
                val nameWithGrams = "$name (${grams.toInt()}g)"
                viewModel.addFoodItem(nameWithGrams, calories)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun TodayCaloriesCard(
    totalCalories: Int,
    dailyGoal: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Today's Calories",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Progress bar showing calories consumed vs goal
            val progress = (totalCalories.toFloat() / dailyGoal.toFloat()).coerceAtMost(1f)
            
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = if (progress > 1f) MaterialTheme.colorScheme.error 
                       else MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Calories text
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$totalCalories kcal consumed",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                
                Text(
                    text = "Goal: $dailyGoal kcal",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            
            // Remaining calories
            val remaining = dailyGoal - totalCalories
            if (remaining > 0) {
                Text(
                    text = "$remaining kcal remaining",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(top = 4.dp)
                )
            } else {
                Text(
                    text = "You've exceeded your daily goal by ${-remaining} kcal",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}