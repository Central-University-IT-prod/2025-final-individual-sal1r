package com.salir.data.mapper

import com.salir.data.local.db.entity.OperationWithGoalName
import com.salir.domain.model.Operation

fun OperationWithGoalName.toDomain(): Operation = Operation(
    id = operation.id,
    sum = operation.sum,
    goalName = goalName,
    date = operation.date,
    comment = operation.comment
)