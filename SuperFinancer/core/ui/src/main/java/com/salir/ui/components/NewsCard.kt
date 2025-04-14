package com.salir.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.salir.domain.model.NewsItem
import com.salir.resources.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun NewsCard(
    newsItem: NewsItem,
    modifier: Modifier = Modifier,
    onClick: (NewsItem) -> Unit = {},
    showSourceAndDate: Boolean = true
) {
    val image = rememberAsyncImagePainter(
        model = newsItem.imageUrl
    )
    val imageState = image.state.collectAsState().value

    ElevatedCard(
        modifier = modifier,
        onClick = { onClick(newsItem) }
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Box(
                modifier = Modifier
                    .height(196.dp)
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
            ) {
                when(imageState) {
                    is AsyncImagePainter.State.Loading -> {
                        ShimmerEffect(
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                    is AsyncImagePainter.State.Success -> {
                        Image(
                            painter = imageState.painter,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    else -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.no_content_here),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = newsItem.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = newsItem.description,
                style = MaterialTheme.typography.bodyMedium
            )

            if (showSourceAndDate) {
                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    Text(
                        text = newsItem.source,
                        style = MaterialTheme.typography.bodySmall,
                        color = LocalContentColor.current.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    newsItem.publishDate?.let { publishDate ->
                        Text(
                            text = remember(newsItem) {
                                publishDate.format(DateTimeFormatter.ofPattern(
                                    "dd.MM.yyyy HH:mm"
                                ))
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = LocalContentColor.current.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NewsCardShimmer(modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            ShimmerEffect(
                modifier = Modifier
                    .height(196.dp)
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextShimmer(
                textStyle = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth(0.9f)
            )

            Spacer(modifier = Modifier.height(4.dp))

            TextShimmer(
                textStyle = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth(),
                lines = 3
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                TextShimmer(
                    textStyle = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.width(128.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                TextShimmer(
                    textStyle = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.width(96.dp)
                )
            }
        }
    }
}


@Preview
@Composable
private fun NewsCardShimmerPreview() {
    NewsCardShimmer()
}

@Composable
private fun NewsCardPreview() {
    Surface {
        NewsCard(
            NewsItem(
                id = "",
                title = "title",
                description = "slfsdfs;fsd ;sdfs;lfs;fdsdlfsd;lf sd;fsd;lfs dlfsfkgrjg eorwgjetog jefg gjtgoergjoerj eorgje orgjer gejrg oergejrg ejrgoe rgjergjergoeg ergoer gsfglksfgldkfgow efwkrog kd fgmegokdrg oekgseorgjrgoisgokvjsdpovmnsosekfmnsrogmsrgokantsrotgbndb;dmsgosrgbmsornsfogbsmdgdjhnsfhusrythiusfniijtghkseglaetgwk bgoet",
                imageUrl = null,
                source = "fsdsdfsdfsd",
                publishDate = LocalDateTime.now(),
                url = ""
            )
        )
    }
}

@Preview
@Composable
private fun NewsCardDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        NewsCardPreview()
    }
}

@Preview
@Composable
private fun NewsCardLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        NewsCardPreview()
    }
}