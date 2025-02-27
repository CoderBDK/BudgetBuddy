package com.coderbdk.budgetbuddy.domain.transaction.usecase

import com.coderbdk.budgetbuddy.data.db.entity.Transaction
import com.coderbdk.budgetbuddy.data.model.TransactionType
import com.coderbdk.budgetbuddy.data.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTotalTransactionAmountUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(type: TransactionType): Flow<Double> {
        return transactionRepository.getTotalTransactionAmount(type)

    }
}