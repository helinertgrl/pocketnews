package com.example.pocketnews.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    navController: NavController
){

    val state = viewModel.uiState

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Ayarlar",
            style = MaterialTheme.typography.headlineMedium
        )
         Text(
             text = "Mevcut kategori: ${state.selectedCategory}",
             style = MaterialTheme.typography.titleMedium
         )
        Text(
            text = "Kategori Seçin:"
        )

        Column {
            state.categories.forEach { option ->
                Row {
                    RadioButton(
                        selected = (state.selectedCategory == option),
                        onClick = {viewModel.onCategorySelected(option)}
                    )
                    Text(text = option)
                }
            }
        }

        Spacer(modifier = Modifier.padding(16.dp))

        Text(
            text = "Mevcut Güncelleme Sıklığı: ${state.selectedInterval} saat",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Güncelleme Sıklığı Seçin:"
        )

        Column {
            state.intervals.forEach { option ->
                Row {
                    RadioButton(
                        selected = (state.selectedInterval == option),
                        onClick = {viewModel.onIntervalSelected(option)}
                    )
                    Text("${option} saat")
                }
            }
        }

        Button(
            onClick = {viewModel.testNotification()}
        ) {
            Text("Bildirimleri Test Et")
        }
    }
}