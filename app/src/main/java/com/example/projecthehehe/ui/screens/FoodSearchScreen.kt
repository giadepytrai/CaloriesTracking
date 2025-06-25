package com.example.projecthehehe.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.projecthehehe.ui.components.AddFoodGramDialog
import com.example.projecthehehe.data.api.FoodDisplayItem
import com.example.projecthehehe.viewmodel.FoodSearchViewModel
import com.example.projecthehehe.viewmodel.CalorieTrackerViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodSearchScreen(
    viewModel: FoodSearchViewModel = viewModel(),
    calorieViewModel: CalorieTrackerViewModel? = null,
    onFoodItemClick: (FoodDisplayItem) -> Unit = {}
) {
    val searchResults by viewModel.searchResults
    val randomFood by viewModel.randomFood
    val isLoading by viewModel.isLoading
    val errorMessage by viewModel.errorMessage
    val searchQuery by viewModel.searchQuery
    val snackbarHostState = remember { SnackbarHostState() }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = viewModel::updateSearchQuery,
                label = { Text("Search for food") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        TextButton(
                            onClick = {
                                viewModel.searchFood(searchQuery)
                            }
                        ) {
                            Text("Search")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Error Message
            errorMessage?.let { error ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Loading Indicator
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                // Content
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Show search results if available, otherwise show random food
                    if (searchResults.isNotEmpty()) {
                        item {
                            Text(
                                text = "Search Results",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        items(searchResults) { foodItem ->
                            FoodDisplayCard(
                                foodItem = foodItem,
                                calorieViewModel = calorieViewModel,
                                snackbarHostState = snackbarHostState,
                                onClick = { onFoodItemClick(foodItem) }
                            )
                        }
                    } else {
                        item {
                            Text(
                                text = "Discover Random Recipes",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        items(randomFood) { foodItem ->
                            FoodDisplayCard(
                                foodItem = foodItem,
                                calorieViewModel = calorieViewModel,
                                snackbarHostState = snackbarHostState,
                                onClick = { onFoodItemClick(foodItem) }
                            )
                        }
                    }
                    
                    if (searchResults.isEmpty() && randomFood.isEmpty() && !isLoading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No recipes found. Try searching for something else!",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FoodDisplayCard(
    foodItem: FoodDisplayItem,
    calorieViewModel: CalorieTrackerViewModel? = null,
    snackbarHostState: SnackbarHostState,
    onClick: () -> Unit
) {
    var showGramDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope() // Move scope here to @Composable context
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Main content row (clickable)
            Row(
                modifier = Modifier
                    .clickable { onClick() }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Food Image
                AsyncImage(
                    model = foodItem.imageUrl,
                    contentDescription = foodItem.title,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Food Details
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = foodItem.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Nutrition Info
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        foodItem.calories?.let { calories ->
                            NutritionChip(
                                label = "Cal",
                                value = calories.toString()
                            )
                        }
                        
                        foodItem.protein?.let { protein ->
                            NutritionChip(
                                label = "Protein",
                                value = "${protein.toInt()}g"
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Additional Info
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        foodItem.servingSize?.let { serving ->
                            Text(
                                text = "${serving.toInt()}g serving",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        foodItem.fiber?.let { fiber ->
                            Text(
                                text = "${fiber.toInt()}g fiber",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }              // Add to Tracker Button
            if (calorieViewModel != null) {
                Divider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    foodItem.calories?.let { calories ->
                        Text(
                            text = "Add calories to tracker",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    Button(
                        onClick = {
                            showGramDialog = true
                        },
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text("Add to Tracker")
                    }
                }
            }
        }
    }
    
    // Gram input dialog
    if (showGramDialog && calorieViewModel != null) {
        AddFoodGramDialog(
            foodItem = foodItem,
            onDismiss = { showGramDialog = false },            onConfirm = { grams, adjustedCalories ->
                // Add to tracker with adjusted calories
                calorieViewModel.addFoodItem("${foodItem.title} (${grams.toInt()}g)", adjustedCalories)
                showGramDialog = false
                
                // Show snackbar confirmation
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "${foodItem.title} (${grams.toInt()}g) added to tracker!",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        )
    }
}

@Composable
fun NutritionChip(
    label: String,
    value: String
) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = "$label: $value",
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}
