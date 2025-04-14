package com.salir.superFinancer.di

import com.salir.superFinancer.di.feature.feedFeatureModule
import com.salir.superFinancer.di.feature.financeFeatureModule
import com.salir.superFinancer.di.feature.homeFeatureModule
import org.koin.dsl.module

val featureModule = module {
    includes(homeFeatureModule, financeFeatureModule, feedFeatureModule)
}