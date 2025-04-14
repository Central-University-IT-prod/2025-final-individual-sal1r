package com.salir.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

fun lerpColorsHsl(colorStart: Color, colorEnd: Color, fraction: Float): Color {
    val startHsl = FloatArray(3)
    val endHsl = FloatArray(3)

    ColorUtils.colorToHSL(colorStart.toArgb(), startHsl)
    ColorUtils.colorToHSL(colorEnd.toArgb(), endHsl)

    val h = startHsl[0] + (endHsl[0] - startHsl[0]) * fraction
    val s = startHsl[1] + (endHsl[1] - startHsl[1]) * fraction
    val l = startHsl[2] + (endHsl[2] - startHsl[2]) * fraction

    val interpolatedArgb = ColorUtils.HSLToColor(floatArrayOf(h, s, l))
    return Color(interpolatedArgb)
}