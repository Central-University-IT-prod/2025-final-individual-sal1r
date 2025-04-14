package com.salir.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TickersSearchResultResponse(
    val result: List<TickerFromSearchResult>
) {
    @Serializable
    data class TickerFromSearchResult(
        val symbol: String
    )
}