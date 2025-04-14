package com.salir.feed.screens

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.salir.feed.viewemodels.AuthViewModel
import com.salir.resources.R
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    authViewModel: AuthViewModel,
    onBackClick: () -> Unit = {},
    signIn: Boolean
) {
    val coroutineScope = rememberCoroutineScope()

    var signInLoading by remember { mutableStateOf(false) }
    var signUpLoading by remember { mutableStateOf(false) }

    AuthScreenContent(
        signIn = signIn,
        loading = signInLoading || signUpLoading,
        onBackClick = onBackClick,
        signInScreen = { SignInScreen(
            loading = signInLoading,
            onSignInClick = { email, password ->
                coroutineScope.launch {
                    signInLoading = true
                    if (authViewModel.signIn(email, password)) onBackClick()
                    signInLoading = false
                }
            }
        ) },
        signUpScreen = { SignUpScreen(
            loading = signUpLoading,
            onSignUpClick = { login, email, password ->
                coroutineScope.launch {
                    signUpLoading = true
                    if (authViewModel.signUp(login, email, password)) onBackClick()
                    signUpLoading = false
                }
            }
        ) }
    )
}

@Composable
private fun AuthScreenContent(
    signIn: Boolean = true,
    loading: Boolean = false,
    onBackClick: () -> Unit = {},
    signInScreen: @Composable () -> Unit = {},
    signUpScreen: @Composable () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        initialPage = if (signIn) 0 else 1,
        pageCount = { 2 }
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column {
            TabRow(
                modifier = Modifier.padding(vertical = 16.dp),
                selectedTabIndex = pagerState.currentPage,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                indicator = { tabPositions ->
                    if (pagerState.currentPage < tabPositions.size) {
                        Box(
                            modifier = Modifier
                                .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                                .padding(horizontal = 16.dp)
                                .clip(MaterialTheme.shapes.medium)
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .fillMaxSize()
                        )
                    }
                },
                divider = {}
            ) {
                Tab(
                    modifier = Modifier
                        .zIndex(1f)
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clip(MaterialTheme.shapes.medium),
                    selected = pagerState.currentPage == 0,
                    onClick = {
                        coroutineScope.launch {
                            if (!loading) pagerState.animateScrollToPage(
                                page = 0,
                                animationSpec = tween(300)
                            )
                        }
                    }
                ) {
                    Text(stringResource(R.string.sign_in))
                }

                Tab(
                    modifier = Modifier
                        .zIndex(1f)
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clip(MaterialTheme.shapes.medium),
                    selected = pagerState.currentPage == 1,
                    onClick = {
                        coroutineScope.launch {
                            if (!loading) pagerState.animateScrollToPage(
                                page = 1,
                                animationSpec = tween(300)
                            )
                        }
                    }
                ) {
                    Text(stringResource(R.string.sign_up))
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                userScrollEnabled = !loading
            ) {
                when (it) {
                    0 -> signInScreen()
                    1 -> signUpScreen()
                }
            }

            ElevatedButton(
                onClick = onBackClick,
                modifier = Modifier
                    .padding(bottom = 16.dp, start = 16.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back_ios_20),
                    contentDescription = null
                )
                Text(
                    text = stringResource(R.string.back),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}


@Composable
private fun AuthScreenPreview() {
    AuthScreenContent(
        signIn = true,
        signInScreen = { SignInScreen() },
        signUpScreen = { SignUpScreen() }
    )
}

@Preview
@Composable
private fun AuthScreenDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        AuthScreenPreview()
    }
}

@Preview
@Composable
private fun AuthScreenLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        AuthScreenPreview()
    }
}