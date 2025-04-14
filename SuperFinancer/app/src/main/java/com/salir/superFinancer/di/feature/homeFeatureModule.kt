package com.salir.superFinancer.di.feature

import com.salir.home.viewmodel.HomeViewModel
import com.salir.home.viewmodel.SearchViewModel
import org.koin.core.module.dsl.scopedOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val homeFeatureModule = module {

    viewModelOf(::HomeViewModel)

    scope(named("SearchContentScreen")) {
        scopedOf(::SearchViewModel)
    }
}