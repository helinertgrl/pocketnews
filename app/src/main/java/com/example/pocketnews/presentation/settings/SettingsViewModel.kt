package com.example.pocketnews.presentation.settings

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Interval
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.pocketnews.data.local.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsState(
    val selectedCategory: String = "",
    val selectedInterval: Int = 4,
    val isNotificationsEnabled: Boolean = false,
    val categories: List<String> = listOf("sports", "technology"),
    val intervals: List<Int> = listOf(2,4,6,12,24)
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager): ViewModel() {
        var uiState by mutableStateOf(SettingsState())
            private set

    var originalState by mutableStateOf(SettingsState())
        private set

    init {
        viewModelScope.launch {
            combine(
                preferencesManager.categoryFlow,
                preferencesManager.updatehoursflow,
                preferencesManager.notificationsFlow
            ) {
                category, hours, notifications ->

                val fetchedState = SettingsState(
                    selectedCategory = category,
                    selectedInterval = hours,
                    isNotificationsEnabled = notifications
                )

                if (originalState.selectedCategory ==""){
                    originalState = fetchedState
                }
                fetchedState
            }.collect{
                state ->
                uiState = state
            }
        }
    }

    val hasChanges: Boolean
        get() = uiState.selectedCategory != originalState.selectedCategory ||
                uiState.selectedInterval != originalState.selectedInterval ||
                uiState.isNotificationsEnabled != originalState.isNotificationsEnabled

    fun onNotificationToggled (enabled: Boolean) {
        uiState = uiState.copy(isNotificationsEnabled = enabled)
    }


    fun onCategorySelected(category: String) {
        uiState = uiState.copy(selectedCategory = category)
    }
    fun onIntervalSelected(interval: Int){
        uiState = uiState.copy(selectedInterval = interval)
    }

    fun onSaveClicked(onComplete: () -> Unit){
        viewModelScope.launch {
            preferencesManager.saveCategoryToDataStore(uiState.selectedCategory)
            preferencesManager.saveHoursToDataStore(uiState.selectedInterval)
            preferencesManager.saveNotificationsToDataStore(uiState.isNotificationsEnabled)

            Log.d("Settings", "WorkManager re-scheduled with ${uiState.selectedInterval} hours")

            originalState = uiState

            onComplete()
        }
    }

    fun testNotification(){
        //TODO: Workmanager eklendiğinde implement edilecek
        Log.d("SettingsViewModel", "Test notification triggered")
    }
}