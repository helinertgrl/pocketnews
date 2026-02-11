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
                apiKey = com.example.pocketnews.BuildConfig.API_KEY
            )
            Result.success(response.articles)
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}