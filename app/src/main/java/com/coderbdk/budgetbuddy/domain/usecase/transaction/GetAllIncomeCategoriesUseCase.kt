package com.coderbdk.budgetbuddy.domain.usecase.transaction

import com.coderbdk.budgetbuddy.data.db.entity.IncomeCategory
import com.coderbdk.budgetbuddy.data.repository.IncomeCategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllIncomeCategoriesUseCase @Inject constructor(
    private val incomeCategoryRepository: IncomeCategoryRepository
) {
    operator fun invoke(): Flow<List<IncomeCategory>> {
        return incomeCategoryRepository.getAllIncomeCategories()
    }
}