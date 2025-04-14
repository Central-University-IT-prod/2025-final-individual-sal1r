package com.salir.superFinancer.navigation.rootNavigation

import kotlinx.serialization.Serializable

sealed class RootNavDestinations {

    @Serializable
    data object Home: RootNavDestinations()

    @Serializable
    data class NewsDetails(
        val url: String,
        val imageUrl: String?,
        val title: String
    ): RootNavDestinations()

    @Serializable
    data class CreatePost(
        val newsUrl: String? = null,
        val newsImage: String? = null,
        val newsTitle: String? = null
    ): RootNavDestinations()
}