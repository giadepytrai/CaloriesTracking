package com.example.projecthehehe.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecthehehe.data.AppDatabase
import com.example.projecthehehe.data.FoodItem
import com.example.projecthehehe.data.UserProfile
import com.example.projecthehehe.data.Gender
import com.example.projecthehehe.data.ActivityLevel
import com.example.projecthehehe.data.CalorieCalculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers

class CalorieTrackerViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val foodDao = database.foodDao()
    private val userProfileDao = database.userProfileDao()

    private val _foodItems = MutableStateFlow<List<FoodItem>>(emptyList())
    val foodItems: StateFlow<List<FoodItem>> = _foodItems.asStateFlow()

    private val _totalCalories = MutableStateFlow(0)
    val totalCalories: StateFlow<Int> = _totalCalories.asStateFlow()

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    private val _bmi = MutableStateFlow<Float?>(null)
    val bmi: StateFlow<Float?> = _bmi.asStateFlow()

    private val _bmiCategory = MutableStateFlow<String?>(null)
    val bmiCategory: StateFlow<String?> = _bmiCategory.asStateFlow()

    private val _dailyCalorieNeeds = MutableStateFlow<Float?>(null)
    val dailyCalorieNeeds: StateFlow<Float?> = _dailyCalorieNeeds.asStateFlow()

    init {
        loadTodayFoodItems()
        loadUserProfile()
    }

    private fun loadTodayFoodItems() {
        val currentTimestamp = System.currentTimeMillis()
        viewModelScope.launch {
            _foodItems.value = foodDao.getFoodItemsForDate(currentTimestamp)
            _totalCalories.value = foodDao.getTotalCaloriesForDate(currentTimestamp) ?: 0
        }
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            val profile = userProfileDao.getUserProfile()
            _userProfile.value = profile
            profile?.let { updateCalculations(it) }
        }
    }

    private fun updateCalculations(profile: UserProfile) {
        val bmi = CalorieCalculator.calculateBMI(profile.height, profile.weight)
        _bmi.value = bmi
        _bmiCategory.value = CalorieCalculator.getBMICategory(bmi)

        _dailyCalorieNeeds.value = CalorieCalculator.calculateDailyCalorieNeeds(
            profile.height,
            profile.weight,
            profile.age,
            profile.gender,
            profile.activityLevel
        )
    }

    fun updateUserProfile(
        height: Float,
        weight: Float,
        age: Int,
        gender: Gender,
        activityLevel: ActivityLevel
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val profile = UserProfile(
                height = height,
                weight = weight,
                age = age,
                gender = gender,
                activityLevel = activityLevel
            )
            userProfileDao.insertUserProfile(profile)
            loadUserProfile() // Reload profile after update
        }
    }

    fun addFoodItem(name: String, calories: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val foodItem = FoodItem(
                name = name,
                calories = calories,
                timestamp = System.currentTimeMillis()
            )
            foodDao.insertFoodItem(foodItem)
            loadTodayFoodItems() // Reload food items after adding
        }
    }

    fun deleteFoodItem(foodItem: FoodItem) {
        viewModelScope.launch(Dispatchers.IO) {
            foodDao.deleteFoodItem(foodItem)
            loadTodayFoodItems() // Reload food items after deleting
        }
    }
} 