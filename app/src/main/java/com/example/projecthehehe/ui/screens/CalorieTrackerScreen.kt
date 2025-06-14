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
) {
    var showAddDialog by remember { mutableStateOf(false) }
    val foodItems by viewModel.foodItems.collectAsState()
    val dailyCalorieNeeds by viewModel.dailyCalorieNeeds.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.calorie_tracker),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        dailyCalorieNeeds?.let { needs ->
            CalorieNeedsCard(
                dailyCalorieNeeds = needs,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

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
    }

    if (showAddDialog) {
        AddFoodDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, calories ->
                viewModel.addFoodItem(name, calories)
                showAddDialog = false
            }
        )
    }
} 