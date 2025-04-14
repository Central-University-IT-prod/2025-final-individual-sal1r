package com.salir.superFinancer.navigation.rootNavigation.homeNavigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.salir.feed.navigation.FeedNavigation
import com.salir.finance.screens.FinanceScreen
import com.salir.home.screens.HomeScreen
import com.salir.superFinancer.navigation.rootNavigation.RootNavDestinations
import com.salir.ui.bottomNavBar.BottomNavBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeNavigation(rootNavController: NavHostController) {
    val homeNavController = rememberNavController()

    val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            BottomNavBar(
                onItemClicked = { item ->
                    homeNavController.navigate(item.route) {
                        popUpTo(homeNavController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                isItemSelected = { item ->
                    currentDestination?.hierarchy?.any {
                        it.hasRoute(item.route::class)
                    } == true
                },
                destinations = HomeNavDestinations.bottomNavBarItems
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(innerPadding)
        ) {
            NavHost(
                navController = homeNavController,
                startDestination = HomeNavDestinations.Home,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None }
            ) {
                composable<HomeNavDestinations.Home> {
                    HomeScreen(
                        vm = koinViewModel(),
                        navigateToNews = { newsItem ->
                            rootNavController.navigate(
                                RootNavDestinations.NewsDetails(
                                    url = newsItem.url,
                                    imageUrl = newsItem.imageUrl,
                                    title = newsItem.title
                                )
                            )
                        }
                    )
                }

                composable<HomeNavDestinations.Feed> {
                    FeedNavigation(
                        navigateToNews = { url, title, image ->
                            rootNavController.navigate(
                                RootNavDestinations.NewsDetails(
                                    url = url,
                                    imageUrl = image,
                                    title = title
                                )
                            )
                        },
                        navigateToCreatePost = {
                            rootNavController.navigate(RootNavDestinations.CreatePost())
                        }
                    )
                }

                composable<HomeNavDestinations.Finance> {
                    FinanceScreen(
                        vm = koinViewModel()
                    )
                }
            }
        }
    }
}