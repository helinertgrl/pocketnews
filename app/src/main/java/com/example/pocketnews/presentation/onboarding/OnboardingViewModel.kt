package com.example.pocketnews.presentation.onboarding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Interval
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketnews.data.local.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


data class OnboardingState(
    val selectedCategory: String = "",
    val selectedInterval: Int = 4,
    val categories: List<String> = listOf("sports", "technology"),
    val intervals: List<Int> = listOf(2,4,6,12,24)
)
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
): ViewModel() {
    var uiState by mutableStateOf(OnboardingState())
        private set

    fun onCategorySelected(category: String) {
        uiState = uiState.copy(selectedCategory = category)
    }

    fun onIntervalSelected(interval: Int) {
        uiState = uiState.copy(selectedInterval = interval)
    }

    fun saveSelection(){
        viewModelScope.launch {
            preferencesManager.saveCategoryToDataStore(uiState.selectedCategory)
            preferencesManager.saveHoursToDataStore(uiState.selectedInterval)
            preferencesManager.saveLaunchToDataStore(false) //Artık ilk açılış değil
        }
    }
}