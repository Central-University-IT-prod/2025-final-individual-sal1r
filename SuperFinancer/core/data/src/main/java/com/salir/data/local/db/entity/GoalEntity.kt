package com.salir.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val sum: Long = 0,
    val goal: Long,
    val date: LocalDate?,
    val isActive: Boolean = true
)