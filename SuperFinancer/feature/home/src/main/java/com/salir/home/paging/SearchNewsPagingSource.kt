package com.salir.home.paging

import com.salir.domain.model.NewsItem
import com.salir.domain.repository.NewsRepository
import com.salir.paging.PagingDataSource
import com.salir.paging.PagingResult
import com.salir.util.Result
import kotlinx.coroutines.flow.collectLatest

class SearchNewsPagingSource(
    private val newsRepository: NewsRepository,
    private val query: String
) : PagingDataSource<NewsItem>() {

    override suspend fun load(limit: Int, page: Int): PagingResult<NewsItem> {
        var result: Result<List<NewsItem>> = Result.Empty

        newsRepository.searchByQuery(query = query, limit = limit, page = page).collectLatest {
            result = it
        }

        return when (result) {
            is Result.Success -> PagingResult.Success(
                data = (result as Result.Success<List<NewsItem>>).data,
                isLast = (result as Result.Success<List<NewsItem>>).data.size < limit
            )
            is Result.Failure -> PagingResult.Error((result as Result.Failure).message)
            is Result.Empty -> PagingResult.Error("Empty", true)
            else -> PagingResult.Error("")
        }
    }
}