package com.example.pocketnews.presentation.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.pocketnews.domain.model.NewsArticle
import java.time.Instant
import java.time.ZonedDateTime


@Composable
fun NewsCard(article: NewsArticle, onClick: () -> Unit){

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        onClick = {
            onClick()
        }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                article.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            if (!article.description.isNullOrEmpty()){
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = article.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = formatTimeAgo(article.publishedAt),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
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