package com.coderbdk.budgetbuddy.domain.usecase.transaction

import com.coderbdk.budgetbuddy.data.model.TransactionFilter
import com.coderbdk.budgetbuddy.data.model.TransactionWithBothCategories
import com.coderbdk.budgetbuddy.data.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFilteredTransactionsWithBothCategoriesUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(filter: TransactionFilter?): Flow<List<TransactionWithBothCategories>> {
        return transactionRepository.getFilteredTransactionsWithBothCategories(filter)

    }
}
