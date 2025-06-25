package com.example.projecthehehe.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecthehehe.R
import com.example.projecthehehe.data.ActivityLevel
import com.example.projecthehehe.data.Gender
import com.example.projecthehehe.data.FitnessGoal
import com.example.projecthehehe.ui.components.*
import com.example.projecthehehe.viewmodel.CalorieTrackerViewModel

@Composable
fun UserProfileScreen(
    onNavigateToTracker: () -> Unit,
    viewModel: CalorieTrackerViewModel = viewModel()
) {
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf(Gender.MALE) }
    var activityLevel by remember { mutableStateOf(ActivityLevel.MODERATE) }
    var fitnessGoal by remember { mutableStateOf(FitnessGoal.MAINTAIN_WEIGHT) }

    val userProfile by viewModel.userProfile.collectAsState()
    val bmi by viewModel.bmi.collectAsState()
    val bmiCategory by viewModel.bmiCategory.collectAsState()
    val dailyCalorieNeeds by viewModel.dailyCalorieNeeds.collectAsState()

    LaunchedEffect(userProfile) {
        userProfile?.let { profile ->
            height = profile.height.toString()
            weight = profile.weight.toString()
            age = profile.age.toString()
            gender = profile.gender
            activityLevel = profile.activityLevel
            fitnessGoal = profile.fitnessGoal
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.user_information),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        UserInfoForm(
            height = height,
            weight = weight,
            age = age,
            gender = gender,
            activityLevel = activityLevel,
            fitnessGoal = fitnessGoal,
            onHeightChange = { height = it },
            onWeightChange = { weight = it },
            onAgeChange = { age = it },
            onGenderChange = { gender = it },
            onActivityLevelChange = { activityLevel = it },
            onFitnessGoalChange = { fitnessGoal = it },
            onSaveClick = {
                val heightFloat = height.toFloatOrNull()
                val weightFloat = weight.toFloatOrNull()
                val ageInt = age.toIntOrNull()
                if (heightFloat != null && weightFloat != null && ageInt != null) {
                    viewModel.updateUserProfile(heightFloat, weightFloat, ageInt, gender, activityLevel, fitnessGoal)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        bmi?.let { bmiValue ->
            BMICard(
                bmi = bmiValue,
                bmiCategory = bmiCategory ?: "",
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        dailyCalorieNeeds?.let { needs ->
            CalorieNeedsCard(
                dailyCalorieNeeds = needs,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onNavigateToTracker,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.go_to_tracker))
        }
    }
} 