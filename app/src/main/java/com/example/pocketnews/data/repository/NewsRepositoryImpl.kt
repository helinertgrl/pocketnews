package com.example.pocketnews.data.repository

import com.example.pocketnews.data.remote.NewsApiService
import com.example.pocketnews.domain.model.NewsArticle
import com.example.pocketnews.domain.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val api: NewsApiService): NewsRepository{
    override suspend fun getTopHeadlines(
        category: String,
        country: String
    ): Result<List<NewsArticle>> {
        return try {
            val response = api.getNewsApiService(
                category = category,
                country = country,
                apiKey = "62a0b36c61884473973acd7d1cf95fc2")
            Result.success(response.articles)
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}