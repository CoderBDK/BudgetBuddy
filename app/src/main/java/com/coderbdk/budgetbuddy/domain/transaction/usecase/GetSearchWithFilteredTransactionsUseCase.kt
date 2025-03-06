package com.coderbdk.budgetbuddy.domain.transaction.usecase

import androidx.paging.PagingData
import com.coderbdk.budgetbuddy.data.db.entity.Transaction
import com.coderbdk.budgetbuddy.data.model.BudgetCategory
import com.coderbdk.budgetbuddy.data.model.BudgetPeriod
import com.coderbdk.budgetbuddy.data.model.TransactionFilter
import com.coderbdk.budgetbuddy.data.model.TransactionType
import com.coderbdk.budgetbuddy.data.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSearchWithFilteredTransactionsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(
        query: String?,
        type: TransactionType?,
        category: BudgetCategory?,
        period: BudgetPeriod?,
        startDate: Long?,
        endDate: Long?,
        isRecurring: Boolean?
    ): Flow<PagingData<Transaction>> {
        return transactionRepository.getFilteredTransactions(
            TransactionFilter(
                query = query,
                type = type,
                category = category,
                period = period,
                startDate = startDate,
                endDate = endDate,
                isRecurring = isRecurring
            )
        )
    }
}
