package com.coderbdk.budgetbuddy.domain.usecase.category

import com.coderbdk.budgetbuddy.data.db.entity.ExpenseCategory
import com.coderbdk.budgetbuddy.data.repository.ExpenseCategoryRepository
import javax.inject.Inject

class InsertExpenseCategoryUseCase @Inject constructor(
    private val expenseCategoryRepository: ExpenseCategoryRepository,
) {
    suspend operator fun invoke(category: ExpenseCategory) {
        expenseCategoryRepository.insertExpenseCategory(category)
    }
}