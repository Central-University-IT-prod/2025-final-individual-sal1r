package com.salir.newsDetails

import android.annotation.SuppressLint
import android.content.Intent
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ComponentActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.salir.resources.R

@SuppressLint("RestrictedApi")
@Composable
fun NewsDetailsScreen(
    url: String,
    title: String,
    onCreatePostClick: () -> Unit = {},
    navigateBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val activity = context as ComponentActivity
    val window = activity.window
    val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

    LaunchedEffect(Unit) {
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    DisposableEffect(Unit) {
        onDispose {
            windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
            windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
        }
    }

    NewsDetailsScreenContent(
        url = url,
        onCreatePostClick = onCreatePostClick,
        onBackClick = navigateBack,
        onShareClick = {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "$title\n$url")
            }

            context.startActivity(
                Intent.createChooser(intent, context.getString(R.string.share_via))
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewsDetailsScreenContent(
    url: String,
    onCreatePostClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onShareClick: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.back)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back_36),
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onCreatePostClick
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_create_post_24),
                            contentDescription = null
                        )
                    }

                    IconButton(
                        onClick = onShareClick
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_share_24),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(innerPadding)
        ) {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        webViewClient = WebViewClient()
                        loadUrl(url)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}


@Composable
private fun NewsDetailsScreenPreview() {
    NewsDetailsScreenContent("")
}

@Preview
@Composable
private fun NewsDetailsScreenDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        NewsDetailsScreenPreview()
    }
}

@Preview
@Composable
private fun NewsDetailsScreenLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        NewsDetailsScreenPreview()
    }
}