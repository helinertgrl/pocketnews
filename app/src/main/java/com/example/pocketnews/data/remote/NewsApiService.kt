package com.example.pocketnews.data.remote

import com.example.pocketnews.domain.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("v2/top-headlines")
    suspend fun getNewsApiService(
        @Query("country") country: String= "us",
        @Query("category") category: String,
        @Query("apiKey") apiKey: String
    ): NewsResponse
}