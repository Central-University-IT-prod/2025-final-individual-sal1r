package com.salir.superFinancer.di.data

import com.salir.data.local.NewsLocalDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val localDataModule = module {
    singleOf(::NewsLocalDataSource)
}