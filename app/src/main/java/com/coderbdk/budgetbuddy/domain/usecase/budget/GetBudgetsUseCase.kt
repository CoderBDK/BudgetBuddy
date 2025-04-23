package com.coderbdk.budgetbuddy.domain.usecase.budget

import com.coderbdk.budgetbuddy.data.db.entity.Budget
import com.coderbdk.budgetbuddy.data.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow

import javax.inject.Inject

class GetBudgetsUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository
) {
    operator fun invoke(): Flow<List<Budget>> {
        return budgetRepository.getBudgets()

    }
}
