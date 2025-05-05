package com.coderbdk.budgetbuddy.domain.usecase.budget

import com.coderbdk.budgetbuddy.data.model.BudgetFilter
import com.coderbdk.budgetbuddy.data.model.BudgetWithCategory
import com.coderbdk.budgetbuddy.data.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFilteredBudgetsWithCategoryUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository
) {
    operator fun invoke(filter: BudgetFilter?): Flow<List<BudgetWithCategory>> {
        return budgetRepository.getFilteredBudgetsWithCategory(filter)

    }
}
