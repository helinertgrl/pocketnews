package com.example.pocketnews.presentation.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.font.FontWeight
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
                title = {Text("Settings",
                    fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
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

            SettingsCard(title = "Appearance") {
                Row (
                    modifier = Modifier.fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text("Dark Mode")
                    Switch(
                        checked = state.isDarkMode,
                        onCheckedChange = {viewModel.onDarkModeToggled(it)}
                    )
                }
            }

            SettingsCard(title = "News Category"){
                state.categories.forEach { option ->
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                            .clickable{viewModel.onCategorySelected(option)}
                    ){
                        RadioButton(
                            selected = (state.selectedCategory == option),
                            onClick = {viewModel.onCategorySelected(option)}
                        )
                        Text(text = option.replaceFirstChar { it.uppercase() })
                    }
                }
            }

            SettingsCard(title = "Check Frequency"){
                state.intervals.forEach { option ->
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                            .clickable{viewModel.onIntervalSelected(option)}
                    ){
                        RadioButton(
                            selected = (state.selectedInterval == option),
                            onClick = {viewModel.onIntervalSelected(option)}
                        )
                        Text("${option} hours")
                    }
                }
            }

            SettingsCard(title = "Notifications"){
                Row (
                    modifier = Modifier.fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text("New news notifications")
                    Switch(
                        checked = state.isNotificationsEnabled,
                        onCheckedChange = { isChecked ->
                            viewModel.onNotificationToggled(isChecked)
                        }
                    )
                }
            }

            Button(
                onClick = {
                    viewModel.onSaveClicked{
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
                    .height(56.dp)
                    .padding(top = 16.dp),
                enabled = viewModel.hasChanges,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save Changes")
            }
        }
    }
}

@Composable
fun SettingsCard(title: String, content: @Composable ColumnScope.() -> Unit){
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp) // EKLE

    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }

    }
}

