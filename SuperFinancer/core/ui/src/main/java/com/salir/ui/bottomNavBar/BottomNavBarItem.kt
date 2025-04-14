package com.salir.ui.bottomNavBar

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class BottomNavBarItem<T: Any>(
    val route: T,
    @StringRes val labelRes: Int,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int
)