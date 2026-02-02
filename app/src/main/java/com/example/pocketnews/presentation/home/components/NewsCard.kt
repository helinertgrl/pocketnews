package com.example.pocketnews.presentation.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pocketnews.domain.model.NewsArticle
import java.time.Instant
import java.time.ZonedDateTime


@Composable
fun NewsCard(article: NewsArticle, onClick: () -> Unit){

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = {
            onClick()
        }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                article.title,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.padding(4.dp))

            Text(
                text = article.description ?:"",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.padding(4.dp))

            Text(
                text = formatTimeAgo(article.publishedAt),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

fun formatTimeAgo(publishedAt: String): String {
    return try {
        val publishedTime = ZonedDateTime.parse(publishedAt).toInstant()
        val now  = Instant.now()
        val duration = java.time.Duration.between(publishedTime, now)

        when{
            duration.toMinutes() < 1 -> "Az önce"
            duration.toMinutes() < 60 -> "${duration.toMinutes()} dakika önce"
            duration.toHours() < 24 -> "${duration.toHours()} saat önce"
            duration.toDays() < 7 -> "${duration.toDays()} gün önce"
            duration.toDays() < 30 -> "${duration.toDays() / 7} hafta önce"
            else -> "${duration.toDays() / 30} ay önce"
        }
    } catch (e: Exception) {
        publishedAt // Hata olursa orijinal tarihi göster
    }
}