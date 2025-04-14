package com.salir.domain.repository

import com.salir.domain.model.NewsItem
import com.salir.util.Result
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getNews(limit: Int, refresh: Boolean): Flow<Result<List<NewsItem>>>

    fun searchByQuery(query: String, limit: Int, page: Int): Flow<Result<List<NewsItem>>>
}