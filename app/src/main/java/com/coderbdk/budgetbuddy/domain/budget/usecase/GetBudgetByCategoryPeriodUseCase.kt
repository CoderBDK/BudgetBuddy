package com.coderbdk.budgetbuddy.domain.budget.usecase

import com.coderbdk.budgetbuddy.data.db.entity.Budget
import com.coderbdk.budgetbuddy.data.model.BudgetCategory
import com.coderbdk.budgetbuddy.data.model.BudgetPeriod
import com.coderbdk.budgetbuddy.data.repository.BudgetRepository
import javax.inject.Inject

class GetBudgetByCategoryPeriodUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository
) {
    suspend operator fun invoke(category: BudgetCategory, period: BudgetPeriod): Budget? {
        return budgetRepository.getBudgetByCategoryAndPeriod(category, period)
    }
}
