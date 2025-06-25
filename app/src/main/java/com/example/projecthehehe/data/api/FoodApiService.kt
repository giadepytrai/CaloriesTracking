package com.example.projecthehehe.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface FoodApiService {
    
    @GET("nutrition")
    suspend fun searchNutrition(
        @Header("X-Api-Key") apiKey: String,
        @Query("query") query: String
    ): Response<CalorieNinjasResponse>
}

data class CalorieNinjasResponse(
    val items: List<CalorieNinjasItem>
)

data class CalorieNinjasItem(
    val name: String,
    val calories: Double,
    val serving_size_g: Double,
    val fat_total_g: Double,
    val fat_saturated_g: Double,
    val protein_g: Double,
    val sodium_mg: Double,
    val potassium_mg: Double,
    val cholesterol_mg: Double,
    val carbohydrates_total_g: Double,
    val fiber_g: Double,
    val sugar_g: Double
)
