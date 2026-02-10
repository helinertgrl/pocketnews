package com.example.pocketnews.presentation.settings

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketnews.data.local.PreferencesManager
import com.example.pocketnews.utils.NotificationHelper
import com.example.pocketnews.worker.NewsWorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsState(
    val selectedCategory: String = "",
    val selectedInterval: Int = 4,
    val isNotificationsEnabled: Boolean = false,
    val categories: List<String> = listOf("sports", "technology"),
    val intervals: List<Int> = listOf(2,4,6,12,24),
    val isDarkMode: Boolean = false,
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    @ApplicationContext private val context: Context
): ViewModel() {
        var uiState by mutableStateOf(SettingsState())
            private set

    var originalState by mutableStateOf(SettingsState())
        private set

    init {
        viewModelScope.launch {
            combine(
                preferencesManager.categoryFlow,
                preferencesManager.updatehoursflow,
                preferencesManager.notificationsFlow,
                preferencesManager.darkModeFlow
            ) {
                category, hours, notifications, darkMode  ->

                val fetchedState = SettingsState(
                    selectedCategory = category,
                    selectedInterval = hours,
                    isNotificationsEnabled = notifications,
                    isDarkMode = darkMode
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
            preferencesManager.saveDarkModeToDataStore(uiState.isDarkMode)

            NewsWorkManager.scheduleNewsCheck(context,uiState.selectedInterval)
            Log.d("SettingsViewModel", "WorkManager re-scheduled with ${uiState.selectedInterval} hours")

            originalState = uiState
            onComplete()
        }
    }

    fun testNotification(){
       viewModelScope.launch {
           val notificationHelper = NotificationHelper(context)
           notificationHelper.sendNewsNotification(
               context = context,
               title = "Test Bildirimi",
               url = "https://www.google.com"
           )
           Log.d("SettingsViewModel", "Test notification sent")
       }
    }

    fun onDarkModeToggled(enabled: Boolean){
        uiState = uiState.copy(isDarkMode = enabled)

        viewModelScope.launch {
            preferencesManager.saveDarkModeToDataStore(enabled)
        }
    }
}