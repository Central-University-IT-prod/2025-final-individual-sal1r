package com.salir.data.model

import com.salir.data.remote.dto.NewsResponse
import com.salir.util.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class NewsCache(
    val news: List<NewsResponse>,
    @Serializable(LocalDateTimeSerializer::class)
    val date: LocalDateTime
)