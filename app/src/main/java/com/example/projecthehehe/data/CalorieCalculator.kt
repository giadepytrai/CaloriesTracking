package com.example.projecthehehe.data

object CalorieCalculator {
    fun calculateBMI(height: Float, weight: Float): Float {
        val heightInMeters = height / 100f
        return weight / (heightInMeters * heightInMeters)
    }

    fun getBMICategory(bmi: Float): String {
        return when {
            bmi < 18.5f -> "Underweight"
            bmi < 25f -> "Normal weight"
            bmi < 30f -> "Overweight"
            else -> "Obese"
        }
    }

    fun calculateDailyCalorieNeeds(
        height: Float,
        weight: Float,
        age: Int,
        gender: Gender,
        activityLevel: ActivityLevel
    ): Float {
        // Calculate BMR using Mifflin-St Jeor Equation
        val bmr = when (gender) {
            Gender.MALE -> 10 * weight + 6.25f * height - 5 * age + 5
            Gender.FEMALE -> 10 * weight + 6.25f * height - 5 * age - 161
        }

        // Apply activity multiplier
        val activityMultiplier = when (activityLevel) {
            ActivityLevel.SEDENTARY -> 1.2f
            ActivityLevel.LIGHTLY_ACTIVE -> 1.375f
            ActivityLevel.MODERATE -> 1.55f
            ActivityLevel.VERY_ACTIVE -> 1.725f
            ActivityLevel.EXTRA_ACTIVE -> 1.9f
        }

        return bmr * activityMultiplier
    }
} 