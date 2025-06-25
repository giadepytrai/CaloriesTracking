package com.example.projecthehehe.data.repository

import com.example.projecthehehe.data.api.FoodApiService
import com.example.projecthehehe.data.api.FoodDisplayItem
import com.example.projecthehehe.data.api.RecipeInformation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FoodRepository(private val apiService: FoodApiService) {    companion object {
        // Your CalorieNinjas API key
        private const val API_KEY = "3SWY+E0iKTSwluthwIyMhg==0k1an1T6e2lRnZGK"
    }
      suspend fun searchFood(query: String): Result<List<FoodDisplayItem>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.searchNutrition(
                    apiKey = API_KEY,
                    query = query
                )
                
                if (response.isSuccessful) {
                    val nutritionResponse = response.body()
                    if (nutritionResponse != null) {
                        val foodItems = nutritionResponse.items.mapIndexed { index, item ->
                            FoodDisplayItem(
                                id = index,
                                title = item.name,
                                imageUrl = null, // CalorieNinjas doesn't provide images
                                calories = item.calories.toInt(),
                                protein = item.protein_g,
                                carbs = item.carbohydrates_total_g,
                                fat = item.fat_total_g,
                                servingSize = item.serving_size_g,
                                fiber = item.fiber_g,
                                sugar = item.sugar_g
                            )
                        }
                        Result.success(foodItems)
                    } else {
                        Result.failure(Exception("Empty response"))
                    }
                } else {
                    Result.failure(Exception("API Error: ${response.code()} - ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
      suspend fun getRandomFood(): Result<List<FoodDisplayItem>> {
        // CalorieNinjas doesn't have random food endpoint, so we'll search for common foods
        val commonFoods = listOf("chicken", "rice", "apple", "banana", "bread", "egg")
        val randomFood = commonFoods.random()
        return searchFood(randomFood)
    }
      suspend fun getRecipeDetails(id: Int): Result<RecipeInformation> {
        return withContext(Dispatchers.IO) {
            try {
                // CalorieNinjas doesn't have recipe details, so we'll create a simple one
                Result.success(
                    RecipeInformation(
                        id = id,
                        title = "Food Item Details",
                        image = null,
                        nutrition = null,
                        summary = "This is nutrition information from CalorieNinjas API. For detailed recipes, consider using a recipe-specific API."
                    )
                )
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
