package com.salir.data.remote.api

import com.salir.data.Hosts
import com.salir.data.remote.dto.CompanyProfileResponse
import com.salir.data.remote.dto.TickerInfoResponse
import com.salir.data.remote.dto.TickersSearchResultResponse
import com.salir.util.AppDispatchers
import com.salir.util.NetworkResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.path
import kotlinx.coroutines.withContext

class FinhubApi(
    private val apiKey: String,
    private val dispatchers: AppDispatchers,
    private val client: HttpClient
) {
    suspend fun getInfoForTicker(ticker: String): NetworkResponse<TickerInfoResponse> = withContext(dispatchers.io) {
        try {
            val response = client.get {
                url {
                    protocol = URLProtocol.HTTPS
                    host = Hosts.FINHUB
                    path("/api/v1/quote")
                    parameters.apply {
                        append("symbol", ticker)
                        append("token", apiKey)
                    }
                }
            }

            if (response.status == HttpStatusCode.OK) {
                NetworkResponse.Success(response.body())
            } else {
                NetworkResponse.Error(response.bodyAsText())
            }
        } catch (e: Exception) {
            NetworkResponse.Error(e.toString())
        }
    }

    suspend fun getCompanyProfileForTicker(ticker: String): NetworkResponse<CompanyProfileResponse> = withContext(dispatchers.io) {
        try {
            val response = client.get {
                url {
                    protocol = URLProtocol.HTTPS
                    host = Hosts.FINHUB
                    path("/api/v1/stock/profile2")
                    parameters.apply {
                        append("symbol", ticker)
                        append("token", apiKey)
                    }
                }
            }

            if (response.status == HttpStatusCode.OK) {
                NetworkResponse.Success(response.body())
            } else {
                NetworkResponse.Error(response.bodyAsText())
            }
        } catch (e: Exception) {
            NetworkResponse.Error(e.toString())
        }
    }

    suspend fun searchTickers(query: String): NetworkResponse<TickersSearchResultResponse> = withContext(dispatchers.io) {
        try {
            val response = client.get {
                url {
                    protocol = URLProtocol.HTTPS
                    host = Hosts.FINHUB
                    path("/api/v1/search")
                    parameters.apply {
                        append("q", query)
                        append("token", apiKey)
                    }
                }
            }

            if (response.status == HttpStatusCode.OK) {
                NetworkResponse.Success(response.body())
            } else {
                NetworkResponse.Error(response.bodyAsText())
            }
        } catch (e: Exception) {
            NetworkResponse.Error(e.toString())
        }
    }
}