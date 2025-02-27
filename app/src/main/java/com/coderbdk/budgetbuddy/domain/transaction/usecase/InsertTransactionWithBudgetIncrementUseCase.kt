package com.coderbdk.budgetbuddy.domain.transaction.usecase

import com.coderbdk.budgetbuddy.data.db.entity.Transaction
import com.coderbdk.budgetbuddy.data.model.TransactionType
import com.coderbdk.budgetbuddy.data.repository.BudgetRepository
import com.coderbdk.budgetbuddy.data.repository.TransactionRepository
import javax.inject.Inject

class InsertTransactionWithBudgetIncrementUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(
        transaction: Transaction
    ) {
        if (transaction.type == TransactionType.EXPENSE) {
            budgetRepository.incrementSpentAmount(
                transaction.category!!,
                transaction.period!!,
                transaction.amount
            )
            transactionRepository.insertTransaction(transaction)
        } else {
            transactionRepository.insertTransaction(
                transaction.copy(
                    category = null,
                    period = null
                )
            )
        }

    }
}