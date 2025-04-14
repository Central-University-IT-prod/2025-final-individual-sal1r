package com.salir.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salir.domain.model.NewsItem
import com.salir.domain.model.TickerInfo
import com.salir.domain.repository.NewsRepository
import com.salir.domain.repository.TickersRepository
import com.salir.home.models.TickerSymbol
import com.salir.util.Result
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(
    private val tickersRepository: TickersRepository,
    private val newsRepository: NewsRepository,
    private val supabase: SupabaseClient
): ViewModel() {
    private val _tickers = MutableStateFlow<Result<List<TickerInfo>>>(Result.Empty)
    val tickers = _tickers.asStateFlow()

    private val _news = MutableStateFlow<Result<List<NewsItem>>>(Result.Empty)
    val news = _news.asStateFlow()

    private var tickersSymbols: List<String> = defaultTickersSymbols

    init {
        viewModelScope.launch {
            try {
                loadTickersSymbols()
            } catch (_: Exception) { }

            loadTickers()
        }
        loadNews()
    }

    fun loadTickers() {
        viewModelScope.launch {
            _loadTickers()
        }
    }

    private suspend fun loadTickersSymbols() {
        tickersSymbols = supabase.from("tickers")
            .select(Columns.ALL)
            .decodeList<TickerSymbol>()
            .map { it.symbol }
    }

    private suspend fun _loadTickers() {
        tickersRepository.getInfoForTickers(tickersSymbols, true).collectLatest {
            _tickers.value = it
        }
    }

    fun refreshTickers() {
        viewModelScope.launch {
            tickersRepository.getInfoForTickers(tickersSymbols, false).collectLatest {
                _tickers.value = it
            }
        }
    }

    fun loadNews() {
        viewModelScope.launch {
            newsRepository.getNews(limit = 10, refresh = false).collectLatest {
                _news.value = it
            }
        }
    }

    fun refreshNews() {
        viewModelScope.launch {
            newsRepository.getNews(limit = 10, refresh = true).collectLatest {
                _news.value = it
            }
        }
    }

    companion object {
        private val defaultTickersSymbols = listOf<String>()
    }
}