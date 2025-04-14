package com.salir.domain.model

import java.time.LocalDateTime

data class NewsItem(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String?,
    val source: String,
    val publishDate: LocalDateTime?,
    val url: String
)
