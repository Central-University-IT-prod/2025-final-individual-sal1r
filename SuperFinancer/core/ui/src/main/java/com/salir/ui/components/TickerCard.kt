package com.salir.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.salir.domain.model.TickerInfo
import com.salir.resources.R
import com.salir.util.Colors

@Composable
fun TickerCard(
    ticker: TickerInfo,
    modifier: Modifier = Modifier,
    expandable: Boolean = false
) {
    val logo = rememberAsyncImagePainter(
        model = ticker.logoUrl
    )
    val logoState = logo.state.collectAsState().value

    ElevatedCard(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            ) {
                when (logoState) {
                    is AsyncImagePainter.State.Loading -> ShimmerEffect(
                        modifier = Modifier.fillMaxSize()
                    )
                    is AsyncImagePainter.State.Success -> Image(
                        painter = logoState.painter,
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                    else -> {
                        Icon(
                            painter = painterResource(R.drawable.ic_info_24),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = if (expandable) Modifier.weight(1f) else Modifier
            ) {
                Text(
                    text = ticker.name,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = if (expandable) Modifier.basicMarquee(
                        iterations = Int.MAX_VALUE
                    ) else Modifier
                )

                Text(
                    text = ticker.companyName,
                    style = MaterialTheme.typography.titleMedium,
                    color = LocalContentColor.current.copy(alpha = 0.7f),
                    modifier = if (expandable) Modifier.basicMarquee(
                        iterations = Int.MAX_VALUE
                    ) else Modifier
                )
            }

            Spacer(modifier = Modifier.width(32.dp))

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "%.2f".format(ticker.currentPrice),
                    style = MaterialTheme.typography.titleMedium
                )

                val color =
                    if (ticker.percentChange > 0) Colors.investProfit
                    else if (ticker.percentChange < 0) Colors.investFail
                    else MaterialTheme.colorScheme.onSurface

                CompositionLocalProvider(
                    LocalContentColor provides color
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(
                                if (ticker.percentChange > 0) R.drawable.ic_small_arrow_top_24
                                else if (ticker.percentChange < 0) R.drawable.ic_small_arrow_down_24
                                else R.drawable.ic_rombik_24
                            ),
                            contentDescription = null
                        )
                        Text(
                            text = "${if (ticker.percentChange > 0) "+" else ""}%.2f%%"
                                .format(ticker.percentChange),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TickerCardShimmer(
    expandable: Boolean = false
) {
    val density = LocalDensity.current

    ElevatedCard {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ShimmerEffect(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = if (expandable) Modifier.weight(1f) else Modifier
            ) {
                TextShimmer(
                    textStyle = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.width(128.dp)
                )

                TextShimmer(
                    textStyle = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.width(96.dp)
                )
            }

            Spacer(modifier = Modifier.width(32.dp))

            Column(
                horizontalAlignment = Alignment.End
            ) {
                TextShimmer(
                    textStyle = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.width(64.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_rombik_24),
                        contentDescription = null
                    )
                    TextShimmer(
                        textStyle = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.width(36.dp)
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun TickerCardShimmerPreview() {
    TickerCardShimmer()
}

@Composable
private fun TickerCardPreview() {
    Surface(
    ) {
        Box {
            TickerCard(
                TickerInfo(
                    name = "AAPL",
                    currentPrice = 1123.42,
                    percentChange = 5.33,
                    companyName = "apple",
                    logoUrl = ""
                )
            )
        }
    }
}

@Preview
@Composable
private fun TickerCardDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        TickerCardPreview()
    }
}

@Preview
@Composable
private fun TickerCardLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        TickerCardPreview()
    }
}