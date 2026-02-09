package com.example.pocketnews.presentation.onboarding

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketnews.data.local.PreferencesManager
import com.example.pocketnews.worker.NewsWorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject


data class OnboardingState(
    val selectedCategory: String = "",
    val selectedInterval: Int = 4,
    val notificationsEnabled: Boolean = true,
    val categories: List<String> = listOf("sports", "technology"),
    val intervals: List<Int> = listOf(2,4,6,12,24)
)
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    @ApplicationContext private val context: Context
): ViewModel() {
    var uiState by mutableStateOf(OnboardingState())
        private set

    private var systemNotificationPermissionGranted = false

    fun onCategorySelected(category: String) {
        uiState = uiState.copy(selectedCategory = category)
    }

    fun onIntervalSelected(interval: Int) {
        uiState = uiState.copy(selectedInterval = interval)
    }

    fun onNotificationToggled(enabled: Boolean){
        uiState = uiState.copy(notificationsEnabled = enabled)
    }

    fun onNotificationPermissionResult(isGranted: Boolean){
        systemNotificationPermissionGranted = isGranted
        //izin reddedilirse switch'i kapat
        if (!isGranted){
            uiState = uiState.copy(notificationsEnabled = false)
        }
        Log.d("OnboardingViewModel","System notification permission: ${isGranted}")
    }

    fun saveSelection(){
        viewModelScope.launch {
            preferencesManager.saveCategoryToDataStore(uiState.selectedCategory)
            preferencesManager.saveHoursToDataStore(uiState.selectedInterval)
            preferencesManager.saveLaunchToDataStore(false) //Artık ilk açılış değil

            val finalNotificationState = systemNotificationPermissionGranted && uiState.notificationsEnabled
            preferencesManager.saveNotificationsToDataStore(finalNotificationState)

            Log.d("OnboardingViewModel", "Final notifications state: $finalNotificationState")
            Log.d("OnboardingViewModel", "System permission: $systemNotificationPermissionGranted, User choice: ${uiState.notificationsEnabled}")

            NewsWorkManager.scheduleNewsCheck(context,uiState.selectedInterval)
            Log.d("OnboardingViewModel","WorkManager scheduled with ${uiState.selectedInterval} hours")
        }
    }
}