package com.coderbdk.budgetbuddy.domain.usecase.category

import com.coderbdk.budgetbuddy.data.db.entity.IncomeCategory
import com.coderbdk.budgetbuddy.data.repository.IncomeCategoryRepository
import javax.inject.Inject

class InsertIncomeCategoryUseCase @Inject constructor(
    private val incomeCategoryRepository: IncomeCategoryRepository,
) {
    suspend operator fun invoke(category: IncomeCategory) {
        incomeCategoryRepository.insertIncomeCategory(category)
    }
}