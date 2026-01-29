package com.example.pocketnews.data.local

import android.accessibilityservice.GestureDescription
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_articles")
data class NewsArticleEntity(
    @PrimaryKey
    val url: String,

    val title: String,

    val description: String,

    val source: String,

    @ColumnInfo(name = "published_At")
    val publishedAt: String,

    val category: String,

    @ColumnInfo(name = "is_Read")
    val isRead: Boolean
)