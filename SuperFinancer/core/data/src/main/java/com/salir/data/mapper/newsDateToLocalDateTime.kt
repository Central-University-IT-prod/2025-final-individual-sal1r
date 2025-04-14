package com.salir.data.mapper

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun newsDateToLocalDateTime(date: String): LocalDateTime =
    ZonedDateTime
        .parse(date, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        .withZoneSameInstant(ZoneId.systemDefault())
        .toLocalDateTime()