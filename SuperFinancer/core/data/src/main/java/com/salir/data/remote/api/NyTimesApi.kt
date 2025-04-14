package com.salir.data.remote.api

import com.salir.data.Hosts
import com.salir.data.remote.dto.NewsResponse
import com.salir.data.remote.dto.SearchNewsResponse
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
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

class NyTimesApi(
    private val apiKey: String,
    private val dispatchers: AppDispatchers,
    private val client: HttpClient,
    private val json: Json
) {
    suspend fun getRelevantNews(limit: Int): NetworkResponse<List<NewsResponse>> = withContext(dispatchers.io) {
        try {
            val resp = client.get {
                url {
                    protocol = URLProtocol.HTTPS
                    host = Hosts.NYTIMES
                    path("svc/news/v3/content/all/your_money.json")
                    parameters.apply {
                        append("limit", limit.toString())
                        append("offset", "0")
                        append("api-key", apiKey)
                    }
                }
            }

            if (resp.status == HttpStatusCode.OK) {
                val jsn = json.parseToJsonElement(resp.bodyAsText())

                val result = json.decodeFromString<List<NewsResponse>>(
                    jsn.jsonObject["results"]!!.jsonArray.toString()
                )
                NetworkResponse.Success(result)
            } else {
                NetworkResponse.Error(resp.bodyAsText())
            }
        } catch (e: Exception) {
            NetworkResponse.Error(e.toString())
        }
    }

    suspend fun searchByQuery(query: String, page: Int): NetworkResponse<SearchNewsResponse> = withContext(dispatchers.io) {
        try {
            val resp = client.get {
                url {
                    protocol = URLProtocol.HTTPS
                    host = Hosts.NYTIMES
                    path("svc/search/v2/articlesearch.json")
                    parameters.apply {
                        append("q", query)
                        append("page", page.toString())
                        append("api-key", apiKey)
                        append("fq", "document_type:(\"article\") AND -section_name:(\"Archives\")")
                        append("sort", "newest")
                    }
                }
            }

            if (resp.status == HttpStatusCode.OK) {
                NetworkResponse.Success(resp.body())
            } else {
                NetworkResponse.Error(resp.bodyAsText())
            }
        } catch (e: Exception) {
            NetworkResponse.Error(e.toString())
        }
    }
}