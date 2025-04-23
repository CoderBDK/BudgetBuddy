package com.coderbdk.budgetbuddy.domain.usecase.transaction

import com.coderbdk.budgetbuddy.data.db.entity.Transaction
import com.coderbdk.budgetbuddy.data.model.TransactionType
import com.coderbdk.budgetbuddy.data.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(type: TransactionType): Flow<List<Transaction>> {
        return transactionRepository.getTransactions(type)

    }
}
