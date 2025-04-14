package com.salir.superFinancer.di

import com.salir.data.repository.FinanceRepositoryImpl
import com.salir.data.repository.NewsRepositoryImpl
import com.salir.data.repository.TickersRepositoryImpl
import com.salir.domain.repository.FinanceRepository
import com.salir.domain.repository.NewsRepository
import com.salir.domain.repository.TickersRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val domainModule = module {
    singleOf(::TickersRepositoryImpl) bind TickersRepository::class

    singleOf(::NewsRepositoryImpl) bind NewsRepository::class

    singleOf(::FinanceRepositoryImpl) bind FinanceRepository::class
}