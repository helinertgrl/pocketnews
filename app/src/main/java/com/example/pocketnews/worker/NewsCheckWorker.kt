package com.example.pocketnews.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.pocketnews.data.local.NewsArticleEntity
import com.example.pocketnews.data.local.NewsDao
import com.example.pocketnews.data.local.PreferencesManager
import com.example.pocketnews.data.remote.NewsApiService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class NewsCheckWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val preferencesManager: PreferencesManager,
    private val newsDao: NewsDao,
    private val apiService: NewsApiService
    ): CoroutineWorker(context,workerParams){

    override suspend fun doWork(): Result {
        try {
            val selectedCategory = preferencesManager.categoryFlow.first()
            val lastDateInDb = newsDao.getLatestPublishDate(selectedCategory) ?: "1970-01-01T00:00:00Z"
            val response = apiService.getNewsApiService("tr",selectedCategory,"62a0b36c61884473973acd7d1cf95fc2")
            val newsArticlesFromApi = response.articles.filter { it.publishedAt > lastDateInDb}

            if (newsArticlesFromApi.isNotEmpty()){

                val newsToInsert = newsArticlesFromApi.map { article ->

                    NewsArticleEntity(
                        title = article.title,
                        description = article.description.orEmpty(),
                        url = article.url,
                        publishedAt = article.publishedAt,
                        category = selectedCategory,
                        source = article.source.name,
                        isRead = false
                    )
                }
                newsDao.insertArticles(newsToInsert)
                println("${newsToInsert.size} adet yeni haber veritabanına başarıyla kaydedildi.")
                //TODO: FIREBASE BİLDİRİMİ BURAYA GLECEK

            }
            return Result.success()

        }catch (e: Exception){
            println("Hata oluştu: ${e.localizedMessage}")
            return Result.retry()
        }
    }
}