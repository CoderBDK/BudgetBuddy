package com.coderbdk.budgetbuddy.domain.usecase.category

import com.coderbdk.budgetbuddy.data.repository.ExpenseCategoryRepository
import javax.inject.Inject

class DeleteExpenseCategoryByIdUseCase @Inject constructor(
    private val expenseCategoryRepository: ExpenseCategoryRepository,
) {
    suspend operator fun invoke(categoryId: Int) {
        expenseCategoryRepository.deleteExpenseCategory(categoryId)
    }
}