package com.salir.superFinancer.di

import com.salir.util.AppDispatchers
import org.koin.dsl.module

val appModule = module {
    single { AppDispatchers() }
}