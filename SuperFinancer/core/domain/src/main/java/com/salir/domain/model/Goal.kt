package com.salir.domain.model

import java.time.LocalDate

data class Goal(
    val id: Long = -1,
    val name: String,
    val sum: Long = 0,
    val goal: Long,
    val date: LocalDate? = null
)