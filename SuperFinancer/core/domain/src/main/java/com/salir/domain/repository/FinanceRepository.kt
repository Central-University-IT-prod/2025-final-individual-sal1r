package com.salir.domain.repository

import com.salir.domain.model.Goal
import com.salir.domain.model.Operation
import kotlinx.coroutines.flow.Flow

interface FinanceRepository {

    fun getOperations(): Flow<List<Operation>>

    fun getGoals(): Flow<List<Goal>>

    suspend fun createGoal(goal: Goal)

    suspend fun createOperation(sum: Long, goalId: Long, comment: String?)

    suspend fun deleteGoal(goal: Goal)
}