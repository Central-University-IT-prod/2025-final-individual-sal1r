package com.salir.data.local.db.entity

import androidx.room.Embedded

data class OperationWithGoalName(
    @Embedded val operation: OperationEntity,
    val goalName: String
)