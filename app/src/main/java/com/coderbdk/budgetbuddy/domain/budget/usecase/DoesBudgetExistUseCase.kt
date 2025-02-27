package com.coderbdk.budgetbuddy.domain.budget.usecase

import com.coderbdk.budgetbuddy.data.model.BudgetCategory
import com.coderbdk.budgetbuddy.data.model.BudgetPeriod
import com.coderbdk.budgetbuddy.data.repository.BudgetRepository
import javax.inject.Inject

class DoesBudgetExistUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository
) {
    suspend operator fun invoke(category: BudgetCategory, period: BudgetPeriod): Boolean {
        return budgetRepository.doesBudgetExist(category, period)
    }
}
