package com.salir.paging

sealed class PagingResult<out T> {
    abstract val isLast: Boolean

    class Success<T>(
        val data: List<T>,
        override val isLast: Boolean = false
    ) : PagingResult<T>()
    class Error(
        val message: String,
        override val isLast: Boolean = false
    ) : PagingResult<Nothing>()
}