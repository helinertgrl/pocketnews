package com.example.pocketnews.domain.model


data class NewsArticle(
    val title: String,
    val description: String?, //The description may be null.
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val source: Source//source is object
)

data class Source(
    val name: String
)