package com.salir.paging

abstract class PagingDataSource<T> {

    abstract suspend fun load(limit: Int, page: Int): PagingResult<T>
}