package com.salir.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CompanyProfileResponse(
    val name: String,
    val ticker: String,
    val logo: String,
)
