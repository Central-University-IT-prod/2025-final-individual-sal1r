package com.salir.domain.model

import java.time.LocalDate

data class Operation(
    val id: Long = -1,
    val sum: Long,
    val goalName: String,
    val date: LocalDate,
    val comment: String? = null,
)