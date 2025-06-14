package com.example.projecthehehe.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.projecthehehe.R

@Composable
fun CalorieNeedsCard(
    dailyCalorieNeeds: Float,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.daily_calorie_needs),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "${String.format("%.0f", dailyCalorieNeeds)} ${stringResource(R.string.unit_kcal)}",
                style = MaterialTheme.typography.headlineLarge
            )
        }
    }
} 