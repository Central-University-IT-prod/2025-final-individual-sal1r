package com.salir.feed.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: Long = 0L,
    val text: String,
    val tags: List<String>,
    val images: List<String>,
    @SerialName("news_title")
    val newsTitle: String? = null,
    @SerialName("news_url")
    val newsUrl: String? = null,
    @SerialName("news_image")
    val newsImage: String? = null,
    @SerialName("user_login")
    val userLogin: String
)