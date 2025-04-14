package com.salir.data.repository

import com.salir.data.local.NewsLocalDataSource
import com.salir.data.mapper.toDomain
import com.salir.data.model.NewsCache
import com.salir.data.remote.api.NyTimesApi
import com.salir.domain.model.NewsItem
import com.salir.domain.repository.NewsRepository
import com.salir.util.NetworkResponse
import com.salir.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime

class NewsRepositoryImpl(
    private val api: NyTimesApi,
    private val newsLocalDataSource: NewsLocalDataSource
) : NewsRepository {

    override fun getNews(limit: Int, refresh: Boolean): Flow<Result<List<NewsItem>>> = flow {
        val cache = newsLocalDataSource.getCache()?.let {
            if (LocalDateTime.now().minusHours(6).isAfter(it.date)) null
            else it
        }

        if ((cache != null) && !refresh) {
            emit(Result.Success(cache.news.map { it.toDomain() }, done = false))
        } else {
            emit(Result.Loading)
        }

        when (val resp = api.getRelevantNews(limit = limit)) {
            is NetworkResponse.Success -> {
                emit(Result.Success(resp.data.map { it.toDomain() }))

                newsLocalDataSource.saveCache(NewsCache(
                    news = resp.data,
                    date = LocalDateTime.now()
                ))
            }
            is NetworkResponse.Error -> {
                emit(Result.Failure(resp.message))
            }
        }
    }

    /*
    * limit всегда равен 10 из-за особенностей API
     */
    override fun searchByQuery(
        query: String, limit: Int, page: Int
    ): Flow<Result<List<NewsItem>>> = flow {
        emit(Result.Loading)

        when (val resp = api.searchByQuery(query = query, page = page)) {
            is NetworkResponse.Success -> {
                emit(Result.Success(resp.data.toDomain()))
            }
            is NetworkResponse.Error -> {
                emit(Result.Failure(resp.message))
            }
        }
    }
}