package com.salir.data.mapper

import com.salir.data.local.db.entity.GoalEntity
import com.salir.domain.model.Goal

fun Goal.toEntity(withId: Boolean): GoalEntity =
    if (withId) GoalEntity(
        id = id,
        name = name,
        sum = sum,
        goal = goal,
        date = date
    )
    else GoalEntity(
        name = name,
        sum = sum,
        goal = goal,
        date = date
    )