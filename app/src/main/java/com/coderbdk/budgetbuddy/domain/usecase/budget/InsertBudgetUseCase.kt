package com.coderbdk.budgetbuddy.domain.usecase.budget

import com.coderbdk.budgetbuddy.data.db.entity.Budget
import com.coderbdk.budgetbuddy.data.repository.BudgetRepository
import javax.inject.Inject

class InsertBudgetUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository
) {
    suspend operator fun invoke(budget: Budget) {
        budgetRepository.insertBudget(budget)
    }
}
