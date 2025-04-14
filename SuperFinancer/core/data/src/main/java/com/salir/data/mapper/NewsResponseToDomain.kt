package com.salir.data.mapper

import com.salir.data.remote.dto.NewsResponse
import com.salir.domain.model.NewsItem

fun NewsResponse.toDomain(): NewsItem = NewsItem(
    id = id,
    title = title,
    description = abstract,
    imageUrl = multimedia?.maxByOrNull { it.width * it.height }?.url,
    source = source,
    publishDate = newsDateToLocalDateTime(pubDate),
    url = url
)