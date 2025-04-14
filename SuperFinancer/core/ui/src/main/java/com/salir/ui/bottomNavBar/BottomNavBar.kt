package com.salir.ui.bottomNavBar

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavBar(
    destinations: List<BottomNavBarItem<*>>,
    onItemClicked: (BottomNavBarItem<*>) -> Unit,
    isItemSelected: (BottomNavBarItem<*>) -> Boolean,
) {
    NavigationBar(
        modifier = Modifier
            .height(72.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
    ) {
        destinations.forEach { destination ->
            val isSelected: Boolean = isItemSelected(destination)

            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemClicked(destination) },
                icon = {
                    AnimatedContent(
                        targetState = isSelected,
                        contentAlignment = Alignment.Center,
                        transitionSpec = {
                            fadeIn(tween(300, easing = EaseOut)) +
                            scaleIn(tween(300, easing = EaseOut), initialScale = 0.92f) togetherWith
                            fadeOut(tween(300, easing = EaseOut))
                        }
                    ) { selected ->
                        Icon(
                            painter = painterResource(
                                if (selected) destination.selectedIcon
                                else destination.unselectedIcon
                            ),
                            contentDescription = null
                        )
                    }
                },
                label = {
                    Text(text = stringResource(destination.labelRes))
                }
            )
        }
    }
}