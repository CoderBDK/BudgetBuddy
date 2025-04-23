package com.coderbdk.budgetbuddy.domain.usecase.budget

import com.coderbdk.budgetbuddy.data.model.BudgetPeriod
import com.coderbdk.budgetbuddy.data.repository.BudgetRepository
import javax.inject.Inject

class DoesBudgetExistUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository
) {
    suspend operator fun invoke(categoryId: Int, period: BudgetPeriod): Boolean {
        return budgetRepository.doesBudgetExist(categoryId, period)
    }
}
