package com.example.pocketnews.di

import android.content.Context
import androidx.room.Room
import com.example.pocketnews.data.local.NewsDao
import com.example.pocketnews.data.local.NewsDatabase
import com.example.pocketnews.data.local.PreferencesManager
import com.example.pocketnews.data.remote.NewsApiService
import com.example.pocketnews.data.remote.RetrofitInstance
import com.example.pocketnews.data.repository.NewsRepositoryImpl
import com.example.pocketnews.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providePreferencesManager(@ApplicationContext context: Context): PreferencesManager{
        return PreferencesManager(context)
    }

    @Provides
    @Singleton
    fun provideNewsApiService(): NewsApiService{
        return RetrofitInstance.api
    }

    @Provides
    @Singleton
    fun provideNewsRepository(api: NewsApiService): NewsRepository{
        return NewsRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideNewsDatabase(@ApplicationContext context: Context): NewsDatabase{
        return Room.databaseBuilder(
            context,
            NewsDatabase::class.java,
            "news_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNewsDao(database: NewsDatabase): NewsDao {
        return database.newsDao()
    }
}
