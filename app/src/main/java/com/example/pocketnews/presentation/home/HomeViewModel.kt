package com.example.pocketnews.presentation.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketnews.data.local.PreferencesManager
import com.example.pocketnews.domain.model.NewsArticle
import com.example.pocketnews.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val articles: List<NewsArticle>): HomeUiState()
    data class Error (val message: String): HomeUiState()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val preferencesManager: PreferencesManager
): ViewModel() {
    var uiState : HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    var isRefreshing by mutableStateOf(false)
        private set

    var selectedCategory by mutableStateOf("general")
        private set

    init {
        viewModelScope.launch {
            preferencesManager.categoryFlow.collect { category ->
                selectedCategory = category
                loadNews(category)
            }
        }
    }

    private fun loadNews(category: String){
        viewModelScope.launch {
            if (!isRefreshing){
                uiState = HomeUiState.Loading
            }

            try {
                var result = newsRepository.getTopHeadlines(category,"us")
                if (result.isSuccess){
                    val articles = result.getOrNull() ?: emptyList()
                    uiState = HomeUiState.Success(articles = articles)
                }
                else{
                    val error = result.exceptionOrNull()?.message ?: "An error occurred"
                    Log.e("HomeViewModel", "❌ Error loading news: $error")
                    uiState = HomeUiState.Error(message = error)
                }
            }
            catch (e: Exception){
                Log.e("HomeViewModel", "❌ Exception: ${e.message}", e)
                uiState = HomeUiState.Error(message = e.message ?: "Unknown error")
            }
        }
    }

    fun refreshNews(){
        viewModelScope.launch {
            isRefreshing = true
            try {
                loadNews(selectedCategory)
            } finally {
                delay(500)
                isRefreshing = false
            }
        }
    }
}