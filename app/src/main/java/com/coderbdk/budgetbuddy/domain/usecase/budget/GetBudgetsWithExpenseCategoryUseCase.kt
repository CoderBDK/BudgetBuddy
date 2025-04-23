package com.coderbdk.budgetbuddy.domain.usecase.budget

import com.coderbdk.budgetbuddy.data.model.BudgetWithCategory
import com.coderbdk.budgetbuddy.data.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBudgetsWithExpenseCategoryUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository
) {
    operator fun invoke(): Flow<List<BudgetWithCategory>> {
        return budgetRepository.getBudgetsWithCategory()

    }
}
