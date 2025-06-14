package com.example.projecthehehe.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projecthehehe.data.Gender
import com.example.projecthehehe.data.ActivityLevel
import com.example.projecthehehe.viewmodel.CalorieTrackerViewModel

@Composable
fun UserProfileScreen(
    viewModel: CalorieTrackerViewModel,
    navController: NavController
) {
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf(Gender.MALE) }
    var selectedActivityLevel by remember { mutableStateOf(ActivityLevel.MODERATE) }

    val userProfile by viewModel.userProfile.collectAsState()
    val bmi by viewModel.bmi.collectAsState()
    val bmiCategory by viewModel.bmiCategory.collectAsState()
    val dailyCalorieNeeds by viewModel.dailyCalorieNeeds.collectAsState()

    LaunchedEffect(userProfile) {
        userProfile?.let { profile ->
            height = profile.height.toString()
            weight = profile.weight.toString()
            age = profile.age.toString()
            selectedGender = profile.gender
            selectedActivityLevel = profile.activityLevel
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "User Information",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Height (cm)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Weight (kg)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Age") },
            modifier = Modifier.fillMaxWidth()
        )

        Text("Gender", style = MaterialTheme.typography.titleMedium)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Gender.values().forEach { gender ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    RadioButton(
                        selected = selectedGender == gender,
                        onClick = { selectedGender = gender }
                    )
                    Text(gender.name)
                }
            }
        }

        Text("Activity Level", style = MaterialTheme.typography.titleMedium)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ActivityLevel.values().forEach { level ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RadioButton(
                        selected = selectedActivityLevel == level,
                        onClick = { selectedActivityLevel = level }
                    )
                    Text(level.name)
                }
            }
        }

        Button(
            onClick = {
                val heightValue = height.toFloatOrNull()
                val weightValue = weight.toFloatOrNull()
                val ageValue = age.toIntOrNull()

                if (heightValue != null && weightValue != null && ageValue != null) {
                    viewModel.updateUserProfile(
                        height = heightValue,
                        weight = weightValue,
                        age = ageValue,
                        gender = selectedGender,
                        activityLevel = selectedActivityLevel
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Profile")
        }

        if (bmi != null && bmiCategory != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("BMI: ${String.format("%.1f", bmi)}")
                    Text("Category: $bmiCategory")
                }
            }
        }

        if (dailyCalorieNeeds != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Daily Calorie Needs: ${String.format("%.0f", dailyCalorieNeeds)} kcal")
                }
            }
        }

        Button(
            onClick = { navController.navigate("tracker") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go to Calorie Tracker")
        }
    }
} 