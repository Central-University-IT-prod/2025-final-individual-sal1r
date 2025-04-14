package com.salir.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    @SerialName("uri")
    val id: String,

    @SerialName("published_date")
    val pubDate: String,

    val title: String,

    val abstract: String,

    val source: String,

    val url: String,

    val multimedia: List<Multimedia>?
)