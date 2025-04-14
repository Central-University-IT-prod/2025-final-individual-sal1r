package com.salir.superFinancer.di.feature

import com.salir.feed.viewemodels.AuthViewModel
import com.salir.feed.viewemodels.CreatePostViewModel
import com.salir.feed.viewemodels.FeedViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val feedFeatureModule = module {
    viewModelOf(::AuthViewModel)

    viewModelOf(::FeedViewModel)

    viewModelOf(::CreatePostViewModel)
}