package com.salir.domain.repository

import com.salir.domain.model.TickerInfo
import com.salir.util.Result
import kotlinx.coroutines.flow.Flow

interface TickersRepository {

    fun getInfoForTickers(
        tickers: List<String>, withLoading: Boolean
    ): Flow<Result<List<TickerInfo>>>

    fun searchByQuery(query: String): Flow<Result<List<TickerInfo>>>
}