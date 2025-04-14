package com.salir.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class Multimedia(
    val url: String,
    val height: Int,
    val width: Int
)