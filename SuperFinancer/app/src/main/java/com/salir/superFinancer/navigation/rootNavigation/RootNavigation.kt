package com.salir.superFinancer.navigation.rootNavigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.salir.feed.screens.CreatePostScreen
import com.salir.newsDetails.NewsDetailsScreen
import com.salir.superFinancer.navigation.rootNavigation.homeNavigation.HomeNavigation
import org.koin.androidx.compose.koinViewModel

@Composable
fun RootNavigation() {
    val rootNavController = rememberNavController()

    NavHost(
        navController = rootNavController,
        startDestination = RootNavDestinations.Home,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable<RootNavDestinations.Home> {
            HomeNavigation(
                rootNavController = rootNavController
            )
        }

        composable<RootNavDestinations.NewsDetails>(
            enterTransition = { slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(300)
            )},
            exitTransition = { slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(300)
            )}
        ) {
            val route = it.toRoute<RootNavDestinations.NewsDetails>()

            NewsDetailsScreen(
                url = route.url,
                title = route.title,
                navigateBack = {
                    if (rootNavController.currentDestination
                        ?.hasRoute(RootNavDestinations.NewsDetails::class) == true
                    ) { rootNavController.navigateUp() }
                },

                onCreatePostClick = {
                    rootNavController.navigate(
                        RootNavDestinations.CreatePost(
                            newsUrl = route.url,
                            newsImage = route.imageUrl,
                            newsTitle = route.title
                        )
                    )
                }
            )
        }

        composable<RootNavDestinations.CreatePost>(
            enterTransition = { slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(300)
            )},
            exitTransition = { slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(300)
            )}
        ) {
            val route = it.toRoute<RootNavDestinations.CreatePost>()

            CreatePostScreen(
                createPostViewModel = koinViewModel(),
                navigateBack = {
                    if (rootNavController.currentDestination
                        ?.hasRoute(RootNavDestinations.CreatePost::class) == true
                    ) {
                        rootNavController.navigateUp()
                    }
                },
                newsImage = route.newsImage,
                newsTitle = route.newsTitle,
                newsUrl = route.newsUrl
            )
        }
    }
}