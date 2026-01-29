package com.example.pocketnews.domain.model


data class NewsArticle(
    val title: String,
    val description: String?, //açıklama boşta olabilir
    val url: String,
    val publishedAt: String,
    val source: Source    //source nesne
)

data class Source(
    val name: String
)