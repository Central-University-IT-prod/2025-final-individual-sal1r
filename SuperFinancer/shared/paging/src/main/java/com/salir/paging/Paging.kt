package com.salir.paging

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class Paging<T>(
    private val limit: Int,
    private val dataSource: PagingDataSource<T>,
    coroutineScope: CoroutineScope
) {
    private var nextPage: Int = 0
    private var isLoading: Boolean = false
    private val _hasMore = MutableStateFlow(true)
    val hasMore = _hasMore.asStateFlow()

    private val _data = MutableStateFlow<List<T>>(emptyList())
    val data = _data.asStateFlow()

    init {
        if (data.value.isEmpty()) {
            coroutineScope.launch { loadNextPage() }
        }
    }

    suspend fun loadNextPage(onSuccess: () -> Unit = {}) {
        if (!isLoading && _hasMore.value) {
            isLoading = true

            val result = dataSource.load(limit, nextPage)

            if (result is PagingResult.Success) {
                _data.value += result.data
                nextPage++
                onSuccess()
            }

            _hasMore.value = !result.isLast
            isLoading = false
        }
    }
}