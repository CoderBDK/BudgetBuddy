package com.coderbdk.budgetbuddy.domain.usecase.transaction

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

            if(transaction.expenseCategoryId != null && transaction.period != null) {
                if(budgetRepository.doesBudgetExist(categoryId = transaction.expenseCategoryId, period = transaction.period)) {
                    budgetRepository.incrementSpentAmount(
                        transaction.expenseCategoryId,
                        transaction.period,
                        transaction.amount
                    )
                }

            }

            transactionRepository.insertTransaction(transaction)
        } else {
            transactionRepository.insertTransaction(
                transaction.copy(
                    expenseCategoryId = null,
                    period = null
                )
            )
        }

    }
}