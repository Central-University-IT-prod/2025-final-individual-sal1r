package com.salir.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TickerInfoResponse(
    val c: Double,
    val d: Double,
    val dp: Double,
    val h: Double,
    val l: Double,
    val o: Double,
    val pc: Double
)