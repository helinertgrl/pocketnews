package com.example.pocketnews.domain.repository

import com.example.pocketnews.domain.model.NewsArticle

interface NewsRepository {
    suspend fun getTopHeadlines(category: String, country: String): Result<List<NewsArticle>>
}