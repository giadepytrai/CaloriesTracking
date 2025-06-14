package com.example.projecthehehe.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.projecthehehe.R
import com.example.projecthehehe.data.ActivityLevel
import com.example.projecthehehe.data.Gender

@Composable
fun UserInfoForm(
    height: String,
    weight: String,
    age: String,
    gender: Gender,
    activityLevel: ActivityLevel,
    onHeightChange: (String) -> Unit,
    onWeightChange: (String) -> Unit,
    onAgeChange: (String) -> Unit,
    onGenderChange: (Gender) -> Unit,
    onActivityLevelChange: (ActivityLevel) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = height,
            onValueChange = onHeightChange,
            label = { Text(stringResource(R.string.height)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = weight,
            onValueChange = onWeightChange,
            label = { Text(stringResource(R.string.weight)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = age,
            onValueChange = onAgeChange,
            label = { Text(stringResource(R.string.age)) },
            modifier = Modifier.fillMaxWidth()
        )

        Text(stringResource(R.string.gender), style = MaterialTheme.typography.titleMedium)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Gender.values().forEach { genderOption ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = gender == genderOption,
                        onClick = { onGenderChange(genderOption) }
                    )
                    Text(genderOption.name)
                }
            }
        }

        Text(stringResource(R.string.activity_level), style = MaterialTheme.typography.titleMedium)
        ActivityLevel.values().forEach { level ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = activityLevel == level,
                    onClick = { onActivityLevelChange(level) }
                )
                Text(
                    when (level) {
                        ActivityLevel.SEDENTARY -> stringResource(R.string.activity_sedentary)
                        ActivityLevel.LIGHTLY_ACTIVE -> stringResource(R.string.activity_light)
                        ActivityLevel.MODERATE -> stringResource(R.string.activity_moderate)
                        ActivityLevel.VERY_ACTIVE -> stringResource(R.string.activity_active)
                        ActivityLevel.EXTRA_ACTIVE -> stringResource(R.string.activity_very_active)
                    }
                )
            }
        }

        Button(
            onClick = onSaveClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_profile))
        }
    }
} 