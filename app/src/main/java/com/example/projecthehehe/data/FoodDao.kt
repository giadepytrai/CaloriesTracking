package com.example.projecthehehe.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Query("SELECT * FROM food_items WHERE date(timestamp/1000, 'unixepoch') = date(:date/1000, 'unixepoch')")
    suspend fun getFoodItemsForDate(date: Long): List<FoodItem>

    @Query("SELECT SUM(calories) FROM food_items WHERE date(timestamp/1000, 'unixepoch') = date(:date/1000, 'unixepoch')")
    suspend fun getTotalCaloriesForDate(date: Long): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodItem(foodItem: FoodItem)

    @Delete
    suspend fun deleteFoodItem(foodItem: FoodItem)
} 