package com.coderbdk.budgetbuddy.domain.usecase.transaction

import androidx.paging.PagingData
import com.coderbdk.budgetbuddy.data.db.entity.Transaction
import com.coderbdk.budgetbuddy.data.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPaginatedTransactionsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(): Flow<PagingData<Transaction>> {
        return transactionRepository.getPagedTransactions()
    }
}
