package com.salir.data.mapper

import android.util.Log
import com.salir.data.remote.dto.SearchNewsResponse
import com.salir.domain.model.NewsItem

fun SearchNewsResponse.toDomain(): List<NewsItem> {
    return this.response.docs.map { art ->
        NewsItem(
            id = art.id,
            title = art.headline.main,
            description = art.abstract ?: "",
            imageUrl = art.multimedia
                ?.maxByOrNull { it.width * it.height }
                ?.let { "https://www.nytimes.com/${it.url}" }
                ?.apply { Log.d("test", this) },
            source = art.source,
            publishDate = null,
            url = art.url
        )
    }
}