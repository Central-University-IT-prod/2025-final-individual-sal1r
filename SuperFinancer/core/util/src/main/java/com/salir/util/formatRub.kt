package com.salir.util

import java.text.NumberFormat
import java.util.Locale

fun formatRub(amount: Number): String {
    val formatter = NumberFormat.getNumberInstance(Locale("ru", "RU"))
    return "${formatter.format(amount)} ${Const.RUBLE_SIGN}"
}