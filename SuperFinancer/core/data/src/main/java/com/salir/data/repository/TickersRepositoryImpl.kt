package com.salir.data.repository

import com.salir.data.mapper.tickerInfoToDomain
import com.salir.data.remote.api.FinhubApi
import com.salir.domain.model.TickerInfo
import com.salir.domain.repository.TickersRepository
import com.salir.util.NetworkResponse
import com.salir.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TickersRepositoryImpl(
    private val api: FinhubApi
) : TickersRepository {

    override fun getInfoForTickers(
        tickers: List<String>,
        withLoading: Boolean
    ): Flow<Result<List<TickerInfo>>> = flow {
        if (withLoading) emit(Result.Loading)

        val resultTickers = mutableListOf<TickerInfo>()

        for (ticker in tickers) {
            val info = api.getInfoForTicker(ticker)
            val company = api.getCompanyProfileForTicker(ticker)

            if (info is NetworkResponse.Success && company is NetworkResponse.Success) {
                resultTickers.add(tickerInfoToDomain(info.data, company.data))
            }
        }

        emit(Result.Success(resultTickers, true))
    }

    override fun searchByQuery(query: String): Flow<Result<List<TickerInfo>>> = flow {
        emit(Result.Loading)

        if (query.isEmpty()) {
            emit(Result.Failure("Query can't be empty"))
            return@flow
        }

        when (val resp = api.searchTickers(query)) {
            is NetworkResponse.Success -> {
                val resultTickers = mutableListOf<TickerInfo>()
                resp.data.result.map { it.symbol }.forEach { ticker ->
                    val info = api.getInfoForTicker(ticker)
                    val company = api.getCompanyProfileForTicker(ticker)

                    if (info is NetworkResponse.Success && company is NetworkResponse.Success) {
                        resultTickers.add(tickerInfoToDomain(info.data, company.data))
                        emit(Result.Success(resultTickers.toList(), false))
                    }
                }

                emit(Result.Success(resultTickers, true))
            }
            is NetworkResponse.Error -> {
                emit(Result.Failure(resp.message))
            }
        }
    }
}