package com.coderbdk.budgetbuddy.data.repository

import androidx.paging.PagingData
import com.coderbdk.budgetbuddy.data.db.entity.Transaction
import com.coderbdk.budgetbuddy.data.model.TransactionFilter
import com.coderbdk.budgetbuddy.data.model.TransactionType
import com.coderbdk.budgetbuddy.data.model.TransactionWithBothCategories
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    suspend fun insertTransaction(transaction: Transaction)
    fun getTransactions(type: TransactionType): Flow<List<Transaction>>
    fun getTotalTransactionAmount(type: TransactionType): Flow<Double>
    fun getRecentTransactions(count: Int): Flow<List<Transaction>>
    fun getRecentTransactionsWithBothCategories(count: Int): Flow<List<TransactionWithBothCategories>>
    fun getPagedTransactions(): Flow<PagingData<Transaction>>
    fun getFilteredTransactions(transactionFilter: TransactionFilter): Flow<PagingData<Transaction>>
    fun getFilteredTransactionsWithBothCategoriesPaging(transactionFilter: TransactionFilter): Flow<PagingData<TransactionWithBothCategories>>
    fun getFilteredTransactionsWithBothCategories(transactionFilter: TransactionFilter?): Flow<List<TransactionWithBothCategories>>
}