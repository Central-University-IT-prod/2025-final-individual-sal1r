package com.salir.feed.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.salir.feed.screens.AuthScreen
import com.salir.feed.screens.FeedScreen
import com.salir.feed.viewemodels.AuthViewModel
import com.salir.feed.viewemodels.FeedViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun FeedNavigation(
    navigateToNews: (url: String, title: String, image: String?) -> Unit = { _, _, _ -> },
    navigateToCreatePost: () -> Unit = {}
) {
    val feedNavController = rememberNavController()

    val authViewModel = koinViewModel<AuthViewModel>()
    val feedViewModel = koinViewModel<FeedViewModel>()

    NavHost(
        navController = feedNavController,
        startDestination = FeedNavDestinations.Feed,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable<FeedNavDestinations.Feed> {
            FeedScreen(
                authViewModel = authViewModel,
                feedViewModel = feedViewModel,
                navigateToSignIn = {
                    feedNavController.navigate(FeedNavDestinations.Auth(true))
                },
                navigateToSignUp = {
                    feedNavController.navigate(FeedNavDestinations.Auth(false))
                },
                navigateToNews = navigateToNews,
                navigateToCreatePost = navigateToCreatePost
            )
        }

        composable<FeedNavDestinations.Auth> {
            val route = it.toRoute<FeedNavDestinations.Auth>()

            AuthScreen(
                authViewModel = authViewModel,
                onBackClick = {
                    feedNavController.popBackStack(
                        route = FeedNavDestinations.Feed,
                        inclusive = false
                    )
                },
                signIn = route.signIn
            )
        }
    }
}