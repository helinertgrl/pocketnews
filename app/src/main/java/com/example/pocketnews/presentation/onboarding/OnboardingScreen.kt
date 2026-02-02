package com.example.pocketnews.presentation.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    onOnboardingComplete: () -> Unit = {}
) {
    val state = viewModel.uiState

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("HOŞ GELDİN")

        state.categories.forEach { option ->
            Row(){
                RadioButton(
                    selected = (state.selectedCategory == option),
                    onClick = {viewModel.onCategorySelected(option)}
                )
                Text(option)
            }
        }

        state.intervals.forEach { option ->
            Row {
                RadioButton(
                    selected = (state.selectedInterval == option),
                    onClick = {viewModel.onIntervalSelected(option)}
                )
                Text("$option saat")
            }
        }

        Button(onClick = {
            viewModel.saveSelection()
            onOnboardingComplete()
        }) {
            Text("KAYDET")
        }

    }
}