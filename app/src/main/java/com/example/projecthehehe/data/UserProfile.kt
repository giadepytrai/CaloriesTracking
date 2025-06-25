package com.example.projecthehehe.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey
    val id: Int = 1, // We'll only have one profile
    val height: Float, // cm
    val weight: Float, // kg
    val age: Int,
    val gender: Gender,
    val activityLevel: ActivityLevel,
    val fitnessGoal: FitnessGoal = FitnessGoal.MAINTAIN_WEIGHT
) 