package com.salir.superFinancer.di.feature

import com.salir.finance.viewmodels.FinanceViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val financeFeatureModule = module {
    viewModelOf(::FinanceViewModel)
}