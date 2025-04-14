package com.salir.feed.navigation

import kotlinx.serialization.Serializable

sealed class FeedNavDestinations {

    @Serializable
    data object Feed : FeedNavDestinations()

    @Serializable
    data class Auth(
        val signIn: Boolean // Определяет, показвать при открытии экрана форму входа или регистрации
    ) : FeedNavDestinations()
}