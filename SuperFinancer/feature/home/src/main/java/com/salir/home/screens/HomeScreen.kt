package com.salir.home.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.salir.domain.model.NewsItem
import com.salir.domain.model.TickerInfo
import com.salir.home.viewmodel.HomeViewModel
import com.salir.resources.R
import com.salir.search.SearchField
import com.salir.ui.components.NewsCard
import com.salir.ui.components.NewsCardShimmer
import com.salir.ui.components.TickerCard
import com.salir.ui.components.TickerCardShimmer
import com.salir.util.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@Composable
fun HomeScreen(
    vm: HomeViewModel,
    navigateToNews: (NewsItem) -> Unit = {}
) {
    // обновление тикеров каждые 5 секунд
    LaunchedEffect(Unit) {
        while (true) {
            vm.refreshTickers()
            delay(5000)
        }
    }

    HomeScreenContent(
        tickers = vm.tickers.collectAsState().value,
        news = vm.news.collectAsState().value,
        onNewsRefresh = vm::refreshNews,
        onNewsItemClick = navigateToNews,
        searchContent = { SearchContentScreen(
            query = it,
            onNewsCardClick = navigateToNews
        ) },
        onLoadTickersClick = vm::loadTickers
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenContent(
    tickers: Result<List<TickerInfo>>,
    news: Result<List<NewsItem>>,
    onNewsItemClick: (NewsItem) -> Unit = {},
    onNewsRefresh: () -> Unit = {},
    onLoadTickersClick: () -> Unit = {},
    searchContent: @Composable (query: String?) -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column {
//            Text(
//                text = stringResource(R.string.home_screen_title),
//                style = MaterialTheme.typography.displaySmall,
//                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
//            )

            var value by rememberSaveable { mutableStateOf("") }
            var expanded by rememberSaveable { mutableStateOf(false) }
            var query by rememberSaveable { mutableStateOf<String?>(null) }

            Spacer(modifier = Modifier.height(8.dp))

            SearchField(
                value = value,
                onValueChange = { value = it },
                expanded = expanded,
                onExpandedChanged = { expanded = it },
                onSearch = { query = value },
                content = { searchContent(query) }
            )

            PullToRefreshBox(
                isRefreshing = false,
                onRefresh = onNewsRefresh,
                state = rememberPullToRefreshState()
            ) {
                LazyColumn {
                    item {
                        val refreshAnim = remember { Animatable(0f) }

                        Row(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Refresh data",
                                style = MaterialTheme.typography.labelMedium,
                                color = LocalContentColor.current.copy(alpha = 0.7f),
                                textAlign = TextAlign.End,
                                modifier = Modifier.weight(1f)
                            )

                            IconButton(
                                onClick = {
                                    onLoadTickersClick()
                                    coroutineScope.launch {
                                        refreshAnim.animateTo(
                                            targetValue = 720f,
                                            animationSpec = tween(1200)
                                        )
                                        refreshAnim.snapTo(0f)
                                    }
                                }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_refresh_20),
                                    contentDescription = null,
                                    modifier = Modifier.graphicsLayer {
                                        rotationZ = refreshAnim.value
                                    },
                                    tint = LocalContentColor.current.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }

                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            when (tickers) {
                                is Result.Success -> {
                                    LazyRow(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        item {
                                            Spacer(modifier = Modifier.width(16.dp))
                                        }

                                        tickers.data.forEach { ticker ->
                                            item(key = ticker.name) {
                                                TickerCard(ticker = ticker)
                                                Spacer(modifier = Modifier.width(16.dp))
                                            }
                                        }
                                    }
                                }

                                is Result.Loading -> {
                                    LazyRow(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        item {
                                            Spacer(modifier = Modifier.width(16.dp))
                                        }

                                        items(count = 4) {
                                            TickerCardShimmer()
                                            Spacer(modifier = Modifier.width(16.dp))
                                        }
                                    }
                                }

                                is Result.Failure -> {
                                    Text(
                                        text = tickers.message
                                    )
                                }

                                is Result.Empty -> {

                                }
                            }
                        }
                    }

                    when (news) {
                        is Result.Success -> {
                            item { Spacer(modifier = Modifier.height(8.dp)) }

                            items(
                                items = news.data,
                                // API может возвращать одинаковые id, поэтому hashCode
                                key = { it.hashCode() }
                            ) { newsItem ->
                                NewsCard(
                                    newsItem = newsItem,
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp, vertical = 8.dp),
                                    onClick = onNewsItemClick
                                )
                            }
                        }

                        is Result.Loading -> {
                            item { Spacer(modifier = Modifier.height(8.dp)) }

                            items(
                                count = 10
                            ) { newsItem ->
                                NewsCardShimmer(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                        }

                        is Result.Failure -> item {
                            Box(
                                modifier = Modifier.fillParentMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No conent, pull to refresh"
                                )
                            }
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}


@Composable
private fun HomeScreenPreview() {
    HomeScreenContent(
        tickers = Result.Success(List(10) {
            TickerInfo(
                name = "AAPL",
                currentPrice = 1123.42,
                percentChange = 5.33,
                companyName = "apple",
                logoUrl = ""
            )
        }),
        news = Result.Success(listOf(
            NewsItem(
                id = "1",
                title = "Первая новость",
                description = "Описание первой новости.",
                imageUrl = "https://example.com/image1.jpg",
                source = "Источник 1",
                publishDate = LocalDateTime.now(),
                url = "https://example.com/news1"
            ),
            NewsItem(
                id = "2",
                title = "Вторая новость",
                description = "Описание второй новости.",
                imageUrl = "https://example.com/image2.jpg",
                source = "Источник 2",
                publishDate = LocalDateTime.now(),
                url = "https://example.com/news2"
            ),
            NewsItem(
                id = "3",
                title = "Третья новость",
                description = "Описание третьей новости.",
                imageUrl = "https://example.com/image3.jpg",
                source = "Источник 3",
                publishDate = LocalDateTime.now(),
                url = "https://example.com/news3"
            )
        ))
    )
}

@Preview
@Composable
private fun HomeScreenDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        HomeScreenPreview()
    }
}

@Preview
@Composable
private fun HomeScreenLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        HomeScreenPreview()
    }
}