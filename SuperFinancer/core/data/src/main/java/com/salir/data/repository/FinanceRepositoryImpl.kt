package com.salir.data.repository

import com.salir.data.local.db.dao.FinanceDao
import com.salir.data.local.db.entity.OperationEntity
import com.salir.data.mapper.toDomain
import com.salir.data.mapper.toEntity
import com.salir.domain.model.Goal
import com.salir.domain.model.Operation
import com.salir.domain.repository.FinanceRepository
import com.salir.util.AppDispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDate

class FinanceRepositoryImpl(
    private val dao: FinanceDao,
    private val dispatchers: AppDispatchers
) : FinanceRepository {

    override fun getOperations(): Flow<List<Operation>> = dao.getOperations().map { list ->
        list.map { it.toDomain() }
    }.flowOn(dispatchers.io)

    override fun getGoals(): Flow<List<Goal>> = dao.getGoals().map { list ->
        list.map { it.toDomain() }
    }.flowOn(dispatchers.io)

    override suspend fun createGoal(goal: Goal): Unit = withContext<Unit>(dispatchers.io) {
        dao.insertGoal(goal.toEntity(withId = false))
    }

    override suspend fun createOperation(
        sum: Long, goalId: Long, comment: String?
    ): Unit = withContext(dispatchers.io) {
        val goal = dao.getGoalById(goalId) ?: throw IllegalArgumentException("Goal not found")

        dao.insertOperation(
            OperationEntity(
                goalId = goalId,
                sum = sum,
                date = LocalDate.now(),
                comment = comment
            )
        )

        dao.updateGoalSum(goalId, goal.sum + sum)
    }

    override suspend fun deleteGoal(goal: Goal): Unit = withContext(dispatchers.io) {
        dao.setGoalInactive(goal.id)

        if (goal.sum > 0) {
            dao.insertOperation(
                OperationEntity(
                    goalId = goal.id,
                    sum = -goal.sum,
                    date = LocalDate.now()
                )
            )
        }
    }
}