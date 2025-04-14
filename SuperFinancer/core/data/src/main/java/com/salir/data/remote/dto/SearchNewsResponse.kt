package com.salir.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchNewsResponse(
    val response: Response
) {

    @Serializable
    data class Response(
        val docs: List<Article>
    )

    @Serializable
    data class Article(
        @SerialName("uri")
        val id: String,
        val abstract: String?,
        @SerialName("web_url")
        val url: String,
        val source: String,
        @SerialName("pub_date")
        val pubDate: String,
        val headline: Headline,
        val multimedia: List<Multimedia>?
    )

    @Serializable
    data class Headline(
        val main: String
    )
}