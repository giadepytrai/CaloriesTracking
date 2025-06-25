package com.example.projecthehehe.data.api

import com.google.gson.annotations.SerializedName

// UI model for displaying food items (simplified for CalorieNinjas)
data class FoodDisplayItem(
    val id: Int,
    val title: String,
    val imageUrl: String?,
    val calories: Int?,
    val protein: Double?,
    val carbs: Double?,
    val fat: Double?,
    val servingSize: Double?,
    val fiber: Double?,
    val sugar: Double?
)

// Simple recipe information for CalorieNinjas
data class RecipeInformation(
    val id: Int,
    val title: String,
    val image: String? = null,
    val nutrition: NutritionInfo? = null,
    val summary: String? = null
)

// Simplified nutrition info
data class NutritionInfo(
    val calories: Double,
    val protein: Double,
    val carbs: Double,
    val fat: Double,
    val fiber: Double,
    val sugar: Double,
    val servingSize: Double
)
