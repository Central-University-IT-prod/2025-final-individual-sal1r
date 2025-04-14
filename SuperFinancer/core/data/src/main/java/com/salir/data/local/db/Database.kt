package com.salir.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.salir.data.local.db.dao.FinanceDao
import com.salir.data.local.db.entity.GoalEntity
import com.salir.data.local.db.entity.OperationEntity

@Database(
    entities = [GoalEntity::class, OperationEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {

    abstract fun financeDao(): FinanceDao
}