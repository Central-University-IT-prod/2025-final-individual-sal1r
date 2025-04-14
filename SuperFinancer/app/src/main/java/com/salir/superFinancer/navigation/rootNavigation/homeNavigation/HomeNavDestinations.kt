package com.salir.superFinancer.navigation.rootNavigation.homeNavigation

import com.salir.resources.R
import com.salir.ui.bottomNavBar.BottomNavBarItem
import kotlinx.serialization.Serializable

sealed class HomeNavDestinations {

    @Serializable
    data object Home: HomeNavDestinations()

    @Serializable
    data object Feed: HomeNavDestinations()

    @Serializable
    data object Finance: HomeNavDestinations()


    companion object {
        val bottomNavBarItems = listOf(
            BottomNavBarItem(
                route = Home,
                labelRes = R.string.title_bottom_nav_bar_home_button,
                selectedIcon = R.drawable.ic_home_filled_24,
                unselectedIcon = R.drawable.ic_home_outlined_24
            ),
            BottomNavBarItem(
                route = Finance,
                labelRes = R.string.title_bottom_nav_bar_finance_button,
                selectedIcon = R.drawable.ic_finance_filled_24,
                unselectedIcon = R.drawable.ic_finance_outlined_24
            ),
            BottomNavBarItem(
                route = Feed,
                labelRes = R.string.title_bottom_nav_bar_feed_button,
                selectedIcon = R.drawable.ic_feed_filled_24,
                unselectedIcon = R.drawable.ic_feed_outlined_24
            ),
        )
    }
}