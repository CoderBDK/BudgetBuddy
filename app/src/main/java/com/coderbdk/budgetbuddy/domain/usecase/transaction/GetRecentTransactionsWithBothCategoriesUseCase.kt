package com.coderbdk.budgetbuddy.domain.usecase.transaction

import com.coderbdk.budgetbuddy.data.model.TransactionWithBothCategories
import com.coderbdk.budgetbuddy.data.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentTransactionsWithBothCategoriesUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(count: Int): Flow<List<TransactionWithBothCategories>> {
        return transactionRepository.getRecentTransactionsWithBothCategories(count)
    }
}