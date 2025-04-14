package com.salir.superFinancer.di.data

import androidx.room.Room
import com.salir.data.local.db.Database
import com.salir.data.local.db.dao.FinanceDao
import org.koin.dsl.module

val dbModule = module {
    single<Database> {
        Room.databaseBuilder(
            context = get(),
            klass = Database::class.java,
            name = "database"
        ).build()
    }

    single<FinanceDao> { get<Database>().financeDao() }
}