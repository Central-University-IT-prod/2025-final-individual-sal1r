package com.salir.finance.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.salir.resources.R
import com.salir.util.formatRub
import com.salir.util.lerpColorsHsl
import kotlin.math.roundToInt

@Composable
fun SavingsWidget(
    accumulated: Long,
    goalsFraction: Float
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_balance_30),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary ,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(7.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = formatRub(accumulated),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = stringResource(R.string.money_accumulated),
                        style = MaterialTheme.typography.titleSmall,
                        color = LocalContentColor.current.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val inactiveColor = MaterialTheme.colorScheme.surfaceContainerHighest
                val color = lerpColorsHsl(Color.Red, Color.Green, goalsFraction)

                Box(
                    modifier = Modifier
                        .drawWithCache {
                            onDrawBehind {
                                drawCircle(
                                    color = inactiveColor,
                                    style = Stroke(
                                        width = 6.dp.toPx(),
                                        cap = StrokeCap.Round
                                    )
                                )
                                
                                drawArc(
                                    color = color,
                                    startAngle = -90f,
                                    sweepAngle = 360f * goalsFraction,
                                    useCenter = false,
                                    style = Stroke(
                                        width = 6.dp.toPx(),
                                        cap = StrokeCap.Round
                                    ),
                                    topLeft = Offset.Zero,
                                    size = size
                                )
                            }
                        }
                        .size(44.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_flag_30),
                        contentDescription = null,
                        tint = color
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "${(goalsFraction * 100).roundToInt()}%",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = stringResource(R.string.goals_achieved),
                        style = MaterialTheme.typography.titleSmall,
                        color = LocalContentColor.current.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}


@Composable
private fun SavingsWidgetPreview() {
    Surface {
        SavingsWidget(
            accumulated = 300,
            goalsFraction = 0.7f
        )
    }
}

@Preview
@Composable
private fun SavingsWidgetDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        SavingsWidgetPreview()
    }
}

@Preview
@Composable
private fun SavingsWidgetLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        SavingsWidgetPreview()
    }
}