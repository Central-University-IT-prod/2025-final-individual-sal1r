package com.salir.feed.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.salir.feed.models.Post
import com.salir.resources.R
import com.salir.ui.components.ShimmerEffect
import kotlin.math.ceil

@Composable
fun PostCard(
    post: Post,
    onNewsClick: (url: String, title: String, image: String?) -> Unit = { _, _, _ -> }
) {
    var selectedImageUrl by remember { mutableStateOf<String?>(null) }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.posted_by, post.userLogin),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            val backgroundColor = CardDefaults.elevatedCardColors().containerColor
            val brush = remember {
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        backgroundColor,
                    )
                )
            }

            var expanded by remember { mutableStateOf(false) }
            var didOverflowHeight by remember { mutableStateOf(false) }

            Box(
                modifier = Modifier
                    .clickable { expanded = !expanded }
                    .animateContentSize()
            ) {
                Text(
                    text = post.text,
                    maxLines = if (expanded) Int.MAX_VALUE else 3,
                    onTextLayout = {
                        didOverflowHeight = it.didOverflowHeight
                    }
                )

                /*
                градиент указвает на то, что текст можно
                развернуть, в ином случае он мешает и путает,
                поэтому он показывается только если текст
                слишком длинный
                 */
                if (didOverflowHeight) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                            .background(brush)
                            .align(Alignment.BottomCenter)
                    )
                }
            }

            if (post.tags.isNotEmpty()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                ) {
                    post.tags.forEach { tag ->
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = tag,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }

            if (post.images.isNotEmpty()) {
                LazyVerticalGrid(
                    userScrollEnabled = false,
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        // высчитываем отношение сторон так, чтобы высота всегда соответсвовала
                        // количеству фото, т.к. LazyVerticalGrid требует явно заданной высоты
                        .aspectRatio(3f / ceil((post.images.size / 3f)))
                        .clip(MaterialTheme.shapes.medium)
                ) {
                    itemsIndexed(
                        items = post.images,
                        key = { _, it -> it }
                    ) { index, imageUrl ->
                        val painter = rememberAsyncImagePainter(imageUrl)
                        val painterState by painter.state.collectAsState()

                        Box(
                            modifier = Modifier
                                .padding(1.dp)
                                .fillMaxSize()
                                .aspectRatio(1f)
                                /* сложно объяснить, но этот обрезает уголки таким образом,
                                   чтобы по краям они были всегда скруглены */
                                .clip(MaterialTheme.shapes.medium.copy(
                                    topStart = CornerSize(0.dp),
                                    topEnd =
                                        if (index ==post.images.lastIndex && index < 2) MaterialTheme.shapes.medium.topEnd
                                        else CornerSize(0.dp),
                                    bottomStart = CornerSize(0.dp),
                                    bottomEnd =
                                        if (
                                            index == post.images.lastIndex && post.images.size % 3 != 0
                                            || (index == 2 && post.images.size < 6)
                                            || (index == 5 && post.images.size < 9)
                                        ) MaterialTheme.shapes.medium.bottomEnd
                                        else CornerSize(0.dp),

                                ))
                                .clickable { selectedImageUrl = imageUrl }
                        ) {
                            when (painterState) {
                                is AsyncImagePainter.State.Loading -> {
                                    ShimmerEffect(
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                                is AsyncImagePainter.State.Success -> {
                                    Image(
                                        painter = painter,
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                else -> {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                                            .padding(8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = stringResource(R.string.no_content_here),
                                            style = MaterialTheme.typography.titleMedium,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (post.newsTitle != null && post.newsUrl != null) {
                Card(
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNewsClick(post.newsUrl, post.newsTitle, post.newsImage) }
                            .padding(8.dp)
                            .height(IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val painter = rememberAsyncImagePainter(post.newsImage)
                        val painterState by painter.state.collectAsState()

                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(MaterialTheme.shapes.medium)
                        ) {
                            when (painterState) {
                                is AsyncImagePainter.State.Loading -> {
                                    ShimmerEffect(
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                                is AsyncImagePainter.State.Success -> {
                                    Image(
                                        painter = painter,
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                else -> {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_feed_outlined_24),
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                        }
                        Text(
                            text = post.newsTitle,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .aspectRatio(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_open_24),
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
    }

    if (selectedImageUrl != null) {
        val painter = rememberAsyncImagePainter(selectedImageUrl)
        val painterState by painter.state.collectAsState()

        var scale by remember { mutableStateOf(1f) }
        var offset by remember { mutableStateOf(Offset.Zero) }

        Dialog(
            onDismissRequest = { selectedImageUrl = null },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            ),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f))
                    .pointerInput(Unit) {
                        awaitEachGesture {
                            while (true) {
                                val event = awaitPointerEvent()

                                if (event.changes.size < 2) continue

                                event.changes.forEach { it.consume() }

                                val zoom = event.calculateZoom()
                                val centroid = event.calculateCentroid().let {
                                    if (it == Offset.Unspecified) Offset(0f, 0f)
                                    else it
                                }
                                val pan = event.calculatePan()

                                val newScale = scale + scale * (zoom - 1f)
                                val zoomOffset = (centroid - offset) * (1f - zoom)

                                scale = newScale
                                offset += pan + zoomOffset
                            }
                        }
                    }
            ) {
                IconButton(
                    onClick = { selectedImageUrl = null },
                    modifier = Modifier
                        .zIndex(1f)
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close_24),
                        contentDescription = null
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            translationX = offset.x
                            translationY = offset.y
                            transformOrigin = TransformOrigin(0f, 0f)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    when (painterState) {
                        is AsyncImagePainter.State.Loading -> {
                            ShimmerEffect(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.4f)
                            )
                        }

                        is AsyncImagePainter.State.Success -> {
                            Image(
                                painter = painter,
                                contentDescription = null,
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        else -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.4f)
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
            }
        }
    }
}


@Composable
private fun PostCardPreview() {
    Surface {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PostCard(
                post = Post(
                    id = 1L,
                    text = "Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным",
                    tags = List(20) { "tag$it" },
                    images = listOf("1", "2", "3", "4", "5"),
                    userLogin = "user123"
                )
            )
            PostCard(
                post = Post(
                    id = 1L,
                    text = "Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным",
                    tags = List(20) { "tag$it" },
                    images = listOf("1"),
                    newsTitle = "Заголовок новости\ndggdfgdfgdfgdfgd",
                    newsUrl = "",
                    newsImage = "",
                    userLogin = "user123"
                )
            )
        }
    }
}

@Preview
@Composable
private fun PostCardDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        PostCardPreview()
    }
}

@Preview
@Composable
private fun PostCardLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        PostCardPreview()
    }
}