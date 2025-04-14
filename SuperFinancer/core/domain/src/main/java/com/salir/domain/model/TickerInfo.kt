package com.salir.domain.model

data class TickerInfo(
    val name: String,
    val currentPrice: Double,
    val percentChange: Double,
    val companyName: String,
    val logoUrl: String,
)
