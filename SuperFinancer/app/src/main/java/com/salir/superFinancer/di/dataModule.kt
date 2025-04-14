package com.salir.superFinancer.di

import com.salir.superFinancer.di.data.dbModule
import com.salir.superFinancer.di.data.localDataModule
import com.salir.superFinancer.di.data.networkModule
import org.koin.dsl.module

val dataModule = module {
    includes(dbModule, networkModule, localDataModule)
}