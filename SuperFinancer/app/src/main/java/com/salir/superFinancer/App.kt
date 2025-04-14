package com.salir.superFinancer

import android.app.Application
import com.salir.superFinancer.di.appModule
import com.salir.superFinancer.di.dataModule
import com.salir.superFinancer.di.domainModule
import com.salir.superFinancer.di.featureModule
import org.koin.android.ext.koin.androidContext
import org.koin.androix.startup.KoinStartup
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.koinConfiguration

@OptIn(KoinExperimentalAPI::class)
class App: Application(), KoinStartup {

    override fun onKoinStartup() = koinConfiguration {
        androidContext(this@App)
        modules(appModule, domainModule, dataModule, featureModule)
    }
}