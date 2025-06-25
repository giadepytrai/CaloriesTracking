package com.example.projecthehehe.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecthehehe.data.api.FoodDisplayItem
import com.example.projecthehehe.data.api.RecipeInformation
import com.example.projecthehehe.data.network.NetworkModule
import com.example.projecthehehe.data.repository.FoodRepository
import kotlinx.coroutines.launch

class FoodSearchViewModel : ViewModel() {
    
    private val repository = FoodRepository(NetworkModule.foodApiService)
    
    private val _searchResults = mutableStateOf<List<FoodDisplayItem>>(emptyList())
    val searchResults: State<List<FoodDisplayItem>> = _searchResults
    
    private val _randomFood = mutableStateOf<List<FoodDisplayItem>>(emptyList())
    val randomFood: State<List<FoodDisplayItem>> = _randomFood
    
    private val _selectedRecipe = mutableStateOf<RecipeInformation?>(null)
    val selectedRecipe: State<RecipeInformation?> = _selectedRecipe
    
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading
    
    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage
    
    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery
    
    init {
        // Load some random recipes when the ViewModel is created
        loadRandomFood()
    }
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun searchFood(query: String) {
        if (query.isBlank()) return
        
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            repository.searchFood(query).fold(
                onSuccess = { results ->
                    _searchResults.value = results
                    _isLoading.value = false
                },
                onFailure = { error ->
                    _errorMessage.value = error.message ?: "Unknown error occurred"
                    _isLoading.value = false
                }
            )
        }
    }
    
    fun loadRandomFood() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            repository.getRandomFood().fold(
                onSuccess = { results ->
                    _randomFood.value = results
                    _isLoading.value = false
                },
                onFailure = { error ->
                    _errorMessage.value = error.message ?: "Failed to load random recipes"
                    _isLoading.value = false
                }
            )
        }
    }
    
    fun getRecipeDetails(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            repository.getRecipeDetails(id).fold(
                onSuccess = { recipe ->
                    _selectedRecipe.value = recipe
                    _isLoading.value = false
                },
                onFailure = { error ->
                    _errorMessage.value = error.message ?: "Failed to load recipe details"
                    _isLoading.value = false
                }
            )
        }
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
    
    fun clearSelectedRecipe() {
        _selectedRecipe.value = null
    }
}
