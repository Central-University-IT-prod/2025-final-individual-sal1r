package com.salir.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salir.domain.model.NewsItem
import com.salir.domain.model.TickerInfo
import com.salir.domain.repository.NewsRepository
import com.salir.domain.repository.TickersRepository
import com.salir.home.paging.SearchNewsPagingSource
import com.salir.paging.Paging
import com.salir.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModel(
    private val tickersRepository: TickersRepository,
    private val newsRepository: NewsRepository
): ViewModel() {
    private val query = MutableStateFlow<String?>(null)
    private val _tickers = MutableStateFlow<Result<List<TickerInfo>>>(Result.Empty)
    val tickers = _tickers.asStateFlow()

    private val _newsPaging = MutableStateFlow<Paging<NewsItem>?>(null)
    val newsPaging = _newsPaging.asStateFlow()

    init {
        viewModelScope.launch {
            query
                .filterNotNull()
                .flatMapLatest { query -> tickersRepository.searchByQuery(query) }
                .collectLatest { _tickers.value = it }
        }
        viewModelScope.launch {
            query.filterNotNull().collectLatest {
                _newsPaging.value = Paging(
                    limit = 10,
                    dataSource = SearchNewsPagingSource(query = it, newsRepository = newsRepository),
                    coroutineScope = viewModelScope
                )
            }
        }
    }

    fun setQuery(query: String?) {
        this.query.value = query
    }

    fun loadNextNewsPage() {
        viewModelScope.launch {
            _newsPaging.value?.loadNextPage()
        }
    }
}