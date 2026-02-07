package com.example.pocketnews.presentation.home

import android.os.Message
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketnews.data.local.PreferencesManager
import com.example.pocketnews.domain.model.NewsArticle
import com.example.pocketnews.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.compose
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
                var result = newsRepository.getTopHeadlines(category,"tr")
                if (result.isSuccess){
                    result.getOrNull()
                    uiState = HomeUiState.Success(articles = result.getOrNull() ?: emptyList())
                }
                else{
                    result.exceptionOrNull()
                    uiState = HomeUiState.Error(message = result.exceptionOrNull()?.message ?: "Hata oluştu" )
                }
            }
            catch (e: Exception){
                uiState = HomeUiState.Error(message = e.message ?: "Bilinmeyen hata")
            }
        }
    }

    fun refreshNews(){
        viewModelScope.launch {
            isRefreshing = true
            loadNews(selectedCategory)
            isRefreshing = false
        }
    }
}