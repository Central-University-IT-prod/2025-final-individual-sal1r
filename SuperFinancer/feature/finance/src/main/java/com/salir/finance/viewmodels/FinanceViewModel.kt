package com.salir.finance.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salir.domain.model.Goal
import com.salir.domain.model.Operation
import com.salir.domain.repository.FinanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class FinanceViewModel(
    private val financeRepository: FinanceRepository
): ViewModel() {
    val goals: Flow<List<Goal>> = financeRepository.getGoals()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val operations: Flow<List<Operation>> = financeRepository.getOperations()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun createGoal(name: String, goal: Long, date: LocalDate?) {
        viewModelScope.launch {
            financeRepository.createGoal(
                Goal(
                    name = name,
                    goal = goal,
                    date = date
                )
            )
        }
    }

    fun createOperation(sum: Long, goalId: Long, comment: String?) {
        viewModelScope.launch {
            financeRepository.createOperation(
                sum = sum,
                goalId = goalId,
                comment = comment
            )
        }
    }

    fun deleteGoal(goal: Goal) {
        viewModelScope.launch {
            financeRepository.deleteGoal(goal)
        }
    }
}