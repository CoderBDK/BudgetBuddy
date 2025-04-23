package com.coderbdk.budgetbuddy.domain.usecase.transaction

import com.coderbdk.budgetbuddy.data.db.entity.ExpenseCategory
import com.coderbdk.budgetbuddy.data.repository.ExpenseCategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllExpenseCategoriesUseCase @Inject constructor(
    private val expenseCategoryRepository: ExpenseCategoryRepository
) {
    operator fun invoke(): Flow<List<ExpenseCategory>> {
        return expenseCategoryRepository.getAllExpenseCategories()
    }
}