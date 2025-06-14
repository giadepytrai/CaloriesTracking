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
fun BMICard(
    bmi: Float,
    bmiCategory: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.bmi_category),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = String.format("%.1f", bmi),
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = bmiCategory,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
} 