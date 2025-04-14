package com.salir.feed.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.salir.feed.components.PostCard
import com.salir.feed.models.Post
import com.salir.feed.viewemodels.AuthViewModel
import com.salir.feed.viewemodels.FeedViewModel
import com.salir.resources.R
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.serialization.json.jsonPrimitive

@Composable
fun FeedScreen(
    authViewModel: AuthViewModel,
    feedViewModel: FeedViewModel,
    navigateToCreatePost: () -> Unit = {},
    navigateToSignIn: () -> Unit = {},
    navigateToSignUp: () -> Unit = {},
    navigateToNews: (url: String, title: String, image: String?) -> Unit = { _, _, _ -> }
) {
    val postsPaging by feedViewModel.postsPaging.collectAsState()
    val authStatus = authViewModel.authStatus.collectAsState().value
    val hasMore = postsPaging.hasMore.collectAsState().value
    val login =
        if (authStatus is SessionStatus.Authenticated)
            authStatus.session.user?.userMetadata
            ?.getValue("login")?.jsonPrimitive
            ?.content
        else null

    FeedScreenContent(
        hasMore = hasMore,
        login = login,
        onSignInClick = navigateToSignIn,
        onSignUpClick = navigateToSignUp,
        onSignOutClick = authViewModel::signOut,
        onNewsClick = navigateToNews,
        posts = postsPaging.data.collectAsState().value,
        onScrollToPostsEnd = feedViewModel::loadNextPostsPage,
        onCreatePostClick = navigateToCreatePost,
        onRefresh = feedViewModel::refreshPosts
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FeedScreenContent(
    hasMore: Boolean = false,
    posts: List<Post>,
    login: String? = null,
    onCreatePostClick: () -> Unit = {},
    onSignInClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
    onSignOutClick: () -> Unit = {},
    onNewsClick: (url: String, title: String, image: String?) -> Unit = { _, _, _ -> },
    onScrollToPostsEnd: () -> Unit = {},
    onRefresh: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_account_36),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                if (login != null) {
                    Text(
                        text = login,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    IconButton(onClick = onSignOutClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_logout_24),
                            contentDescription = null
                        )
                    }
                } else {
                    TextButton(onClick = onSignInClick) {
                        Text(stringResource(R.string.sign_in))
                    }
                    Text(
                        text = "or",
                        style = MaterialTheme.typography.titleSmall
                    )
                    TextButton(onClick = onSignUpClick) {
                        Text(stringResource(R.string.sign_up))
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                if (login != null) {
                    IconButton(onClick = onCreatePostClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_create_post_24),
                            contentDescription = null
                        )
                    }
                }
            }

            PullToRefreshBox(
                isRefreshing = false,
                state = rememberPullToRefreshState(),
                onRefresh = onRefresh,
                modifier = Modifier.weight(1f)
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(
                        items = posts,
                        key = { _, it -> it.id }
                    ) { index, post ->
                        PostCard(
                            post = post,
                            onNewsClick = onNewsClick
                        )

                        if (index > posts.lastIndex - 2) {
                            LaunchedEffect(Unit) {
                                onScrollToPostsEnd()
                            }
                        }
                    }

                    if (hasMore) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    } else {
                        item {}
                    }
                }
            }
        }
    }
}


@Composable
private fun FeedScreenPreview() {
    FeedScreenContent(
        login = "test",
        posts = List(10) {
            Post(
                id = it.toLong(),
                text = "Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным Текст поста, может быть длинным",
                tags = List(20) { "tag$it" },
                images = listOf("1", "2", "3", "4", "5", "6"),
                newsTitle = "Заголовок новости\ndggdfgdfgdfgdfgd",
                newsUrl = "",
                newsImage = "",
                userLogin = "user123"
            )
        }
    )
}

@Preview
@Composable
private fun FeedScreenDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        FeedScreenPreview()
    }
}

@Preview
@Composable
private fun FeedScreenLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        FeedScreenPreview()
    }
}