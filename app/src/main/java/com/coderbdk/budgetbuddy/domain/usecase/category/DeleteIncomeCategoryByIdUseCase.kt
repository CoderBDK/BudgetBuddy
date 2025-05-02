package com.coderbdk.budgetbuddy.domain.usecase.category

import com.coderbdk.budgetbuddy.data.repository.IncomeCategoryRepository
import javax.inject.Inject

class DeleteIncomeCategoryByIdUseCase @Inject constructor(
    private val incomeCategoryRepository: IncomeCategoryRepository,
) {
    suspend operator fun invoke(categoryId: Int) {
        incomeCategoryRepository.deleteIncomeCategory(categoryId)
    }
}