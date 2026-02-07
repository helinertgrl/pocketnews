package com.example.pocketnews.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    navController: NavController
){

    val state = viewModel.uiState

    Scaffold (
        topBar = {
            TopAppBar(
                title = {Text("Ayarlar")},
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Geri"
                        )
                    }
                }
            )
        }

    ){
        innerpadding ->

        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier.fillMaxSize()
                .padding(16.dp)
                .padding(innerpadding)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Mevcut kategori: ${state.selectedCategory}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Kategori Seçin:"
            )

            Column {
                state.categories.forEach { option ->
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ){
                        RadioButton(
                            selected = (state.selectedCategory == option),
                            onClick = {viewModel.onCategorySelected(option)}
                        )
                        Text(text = option,
                            modifier = Modifier.padding(8.dp))
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Text(
                text = "Mevcut Güncelleme Sıklığı: ${state.selectedInterval} saat",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Güncelleme Sıklığı Seçin:"
            )

            Column {
                state.intervals.forEach { option ->
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ){
                        RadioButton(
                            selected = (state.selectedInterval == option),
                            onClick = {viewModel.onIntervalSelected(option)}
                        )
                        Text("${option} saat",
                            modifier = Modifier.padding(8.dp))
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Text(
                text = "Bildirimler",
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Haber bildirimlerini al")

                Switch(
                    checked = state.isNotificationsEnabled,
                    onCheckedChange = { isChecked ->
                        viewModel.onNotificationToggled(isChecked)
                    }
                )
            }

            Button(
                onClick = {viewModel.testNotification()}
            ) {
                Text("Bildirimleri Test Et")
            }

            Spacer(modifier = Modifier.padding(16.dp))

            Button(
                onClick = {
                    viewModel.onSaveClicked{
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                enabled = viewModel.hasChanges
            ) {
                Text("Kaydet")
            }

        }
    }
}

