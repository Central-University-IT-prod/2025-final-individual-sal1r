package com.salir.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle

@Composable
fun TextShimmer(
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
    lines: Int = 1
) {
    val density = LocalDensity.current
    ShimmerEffect(
        modifier = modifier
            .height(with (density) {
                textStyle.lineHeight.toDp() * lines
            })
            .padding(vertical = with (density) {
                textStyle.lineHeight.toDp() -
                        textStyle.fontSize.toDp()
            } / 2f)
            .clip(CircleShape)
    )
}