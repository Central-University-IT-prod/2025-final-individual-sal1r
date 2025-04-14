package com.salir.superFinancer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.salir.superFinancer.navigation.rootNavigation.RootNavigation
import com.salir.superFinancer.theme.SuperFinancerTheme
import org.koin.androidx.compose.KoinAndroidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            KoinAndroidContext {
                SuperFinancerTheme {
                    RootNavigation()
                }
            }
        }
    }
}