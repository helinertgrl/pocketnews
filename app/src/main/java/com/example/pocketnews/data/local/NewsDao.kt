package com.example.pocketnews.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<NewsArticleEntity>)

    @Query("SELECT * FROM news_articles WHERE category = category ORDER BY published_At DESC")
    fun getArticlesByCategory(category: String): Flow<List<NewsArticleEntity>>

    @Query("SELECT MAX(published_At) FROM news_articles WHERE category = category")
    suspend fun getLatestPublishDate(category: String): String?

    @Query("DELETE FROM NEWS_ARTICLES WHERE category = category")
    suspend fun deleteArticlesByCategory(category: String)
}