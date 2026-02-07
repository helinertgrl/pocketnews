package com.example.pocketnews.worker

import android.content.Context
import android.os.Build
import android.util.Log
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (androidx.core.content.ContextCompat.checkSelfPermission(
                    applicationContext,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != android.content.pm.PackageManager.PERMISSION_GRANTED
                    ) {
                    return Result.failure() //izin yoksa failure
                }
            }

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

                val firstArticle = newsArticlesFromApi.first()
                val notificationHelper = com.example.pocketnews.utils.NotificationHelper(applicationContext)

                notificationHelper.sendNewsNotification(
                    context = applicationContext,
                    title = firstArticle.title,
                    url = firstArticle.url
                )

            }
            return Result.success()

        }catch (e: Exception){
            Log.e("NewsCheckWorker", "Haber kontrolü sırasında hata oluştu: ${e.message}", e)
            return Result.retry()
        }
    }
}