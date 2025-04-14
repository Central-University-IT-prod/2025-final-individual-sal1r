package com.salir.data.mapper

import com.salir.data.local.db.entity.GoalEntity
import com.salir.domain.model.Goal

fun GoalEntity.toDomain(): Goal = Goal(
    id = id,
    name = name,
    sum = sum,
    goal = goal,
    date = date
)