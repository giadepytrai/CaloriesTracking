package com.example.projecthehehe.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromGender(gender: Gender): String {
        return gender.name
    }

    @TypeConverter
    fun toGender(value: String): Gender {
        return try {
            Gender.valueOf(value)
        } catch (e: IllegalArgumentException) {
            Gender.MALE // Default value
        }
    }

    @TypeConverter
    fun fromActivityLevel(activityLevel: ActivityLevel): String {
        return activityLevel.name
    }

    @TypeConverter
    fun toActivityLevel(value: String): ActivityLevel {
        return try {
            ActivityLevel.valueOf(value)
        } catch (e: IllegalArgumentException) {
            ActivityLevel.MODERATE // Default value
        }
    }
} 