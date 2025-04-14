package com.salir.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.salir.data.local.db.entity.GoalEntity
import com.salir.data.local.db.entity.OperationEntity
import com.salir.data.local.db.entity.OperationWithGoalName
import kotlinx.coroutines.flow.Flow

@Dao
interface FinanceDao {

    @Query("SELECT * FROM goals WHERE isActive = 1")
    fun getGoals(): Flow<List<GoalEntity>>

    @Query("""
        SELECT operations.*, goals.name as goalName 
        FROM operations 
        INNER JOIN goals ON operations.goalId = goals.id
    """)
    fun getOperations(): Flow<List<OperationWithGoalName>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: GoalEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOperation(operation: OperationEntity)

    @Query("SELECT * FROM goals WHERE id = :goalId")
    suspend fun getGoalById(goalId: Long): GoalEntity?

    @Query("UPDATE goals SET sum = :newSum WHERE id = :goalId")
    suspend fun updateGoalSum(goalId: Long, newSum: Long)

    @Query("UPDATE goals SET isActive = 0 WHERE id = :goalId")
    suspend fun setGoalInactive(goalId: Long)
}