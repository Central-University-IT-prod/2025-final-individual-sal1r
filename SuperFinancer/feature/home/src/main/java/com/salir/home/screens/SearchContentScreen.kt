package com.salir.home.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.salir.domain.model.NewsItem
import com.salir.domain.model.TickerInfo
import com.salir.home.viewmodel.SearchViewModel
import com.salir.ui.components.NewsCard
import com.salir.ui.components.NewsCardShimmer
import com.salir.ui.components.TickerCard
import com.salir.ui.components.TickerCardShimmer
import com.salir.util.Result
import org.koin.compose.getKoin
import org.koin.core.qualifier.named

@Composable
fun SearchContentScreen(
    query: String?,
    onNewsCardClick: (NewsItem) -> Unit = {}
) {
    val koin = getKoin()
    val scope = remember {
        koin.createScope("SearchContentScreen", named("SearchContentScreen"))
    }

    val vm = remember { scope.get<SearchViewModel>() }

    DisposableEffect(Unit) {
        onDispose { scope.close() }
    }

    LaunchedEffect(query) {
        vm.setQuery(query)
    }

    val newsPaging by vm.newsPaging.collectAsState()

    SearchContentScreenContent(
        tickers = vm.tickers.collectAsState().value,
        news = newsPaging?.data?.collectAsState()?.value,
        hasMoreNews = newsPaging?.hasMore?.collectAsState()?.value ?: false,
        onScrollToNewsEnd = { vm.loadNextNewsPage() },
        onNewsCardClick = onNewsCardClick
    )
}

@Composable
private fun SearchContentScreenContent(
    tickers: Result<List<TickerInfo>>,
    news: List<NewsItem>?,
    hasMoreNews: Boolean,
    onScrollToNewsEnd: () -> Unit = {},
    onNewsCardClick: (NewsItem) -> Unit = {}
) {
    Column {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (tickers) {
                    is Result.Success -> {
                        if (tickers.data.isEmpty()) Text(
                            text = stringResource(com.salir.resources.R.string.no_content_here)
                        )
                        else LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            items(
                                items = tickers.data,
                                key = { it.hashCode() }
                            ) { ticker ->
                                TickerCard(ticker = ticker, expandable = true)
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            if (!tickers.done) {
                                item {
                                    TickerCardShimmer(expandable = true)
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }
                    }

                    is Result.Empty -> {}

                    is Result.Loading -> Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        TickerCardShimmer(expandable = true)
                    }

                    is Result.Failure -> Text(text = tickers.message)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (news != null) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                itemsIndexed(
                    items = news,
                    // API может возвращать одинаковые id, поэтому hashCode
                    key = { _, it -> it.hashCode() }
                ) { index, newsItem ->
                    NewsCard(
                        newsItem = newsItem,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        onClick = onNewsCardClick
                    )

                    if (index > news.lastIndex - 2) {
                        LaunchedEffect(Unit) {
                            onScrollToNewsEnd()
                        }
                    }
                }

                if (hasMoreNews) {
                    items(10) {
                        NewsCardShimmer(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun SearchContentScreenPreview() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
//        SearchContentScreenContent(
//            tickers = Result.Success(List(10) {
//                TickerInfo(
//                    name = "AAPL",
//                    currentPrice = 1123.42,
//                    percentChange = 5.33,
//                    companyName = "apple",
//                    logoUrl = ""
//                )
//            })
//        )
    }
}

@Preview
@Composable
private fun SearchContentScreenDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        SearchContentScreenPreview()
    }
}

@Preview
@Composable
private fun SearchContentScreenLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        SearchContentScreenPreview()
    }
}