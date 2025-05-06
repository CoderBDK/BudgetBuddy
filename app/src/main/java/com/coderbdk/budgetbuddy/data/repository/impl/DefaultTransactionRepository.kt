package com.coderbdk.budgetbuddy.data.repository.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.coderbdk.budgetbuddy.data.db.dao.BudgetDao
import com.coderbdk.budgetbuddy.data.db.dao.TransactionDao
import com.coderbdk.budgetbuddy.data.db.entity.Transaction
import com.coderbdk.budgetbuddy.data.model.TransactionFilter
import com.coderbdk.budgetbuddy.data.model.TransactionType
import com.coderbdk.budgetbuddy.data.model.TransactionWithBothCategories
import com.coderbdk.budgetbuddy.data.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultTransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao,
) : TransactionRepository {
    override suspend fun insertTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction)
    }

    override fun getTransactions(type: TransactionType): Flow<List<Transaction>> {
        return transactionDao.getTransactions(type)
    }

    override fun getRecentTransactions(count: Int): Flow<List<Transaction>> {
        return transactionDao.getRecentTransactions(count)
    }

    override fun getRecentTransactionsWithBothCategories(count: Int): Flow<List<TransactionWithBothCategories>> {
        return transactionDao.getRecentTransactionsWithBothCategories(count)
    }
    override fun getTotalTransactionAmount(type: TransactionType): Flow<Double> {
        return transactionDao.getTotalTransactionAmount(type)
    }

    override fun getPagedTransactions(): Flow<PagingData<Transaction>> {
        return Pager(
            config = PagingConfig(pageSize = 30, enablePlaceholders = false),
            pagingSourceFactory = { transactionDao.getPagedTransactions() }
        ).flow
    }

    override fun getFilteredTransactions(transactionFilter: TransactionFilter): Flow<PagingData<Transaction>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                transactionDao.getFilteredTransactionsPaging(
                    transactionFilter.query,
                    transactionFilter.type,
                    transactionFilter.expenseCategoryId,
                    transactionFilter.incomeCategoryId,
                    transactionFilter.period,
                    transactionFilter.startDate,
                    transactionFilter.endDate,
                    transactionFilter.isRecurring
                )
            }
        ).flow
    }

    override fun getFilteredTransactionsWithBothCategoriesPaging(transactionFilter: TransactionFilter): Flow<PagingData<TransactionWithBothCategories>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                transactionDao.getFilteredTransactionsWithBothCategoriesPaging(
                    transactionFilter.query,
                    transactionFilter.type,
                    transactionFilter.expenseCategoryId,
                    transactionFilter.incomeCategoryId,
                    transactionFilter.period,
                    transactionFilter.startDate,
                    transactionFilter.endDate,
                    transactionFilter.isRecurring
                )
            }
        ).flow
    }

    override fun getFilteredTransactionsWithBothCategories(transactionFilter: TransactionFilter?): Flow<List<TransactionWithBothCategories>> {
        return transactionDao.getFilteredTransactionsWithBothCategories(
            transactionFilter?.query,
            transactionFilter?.type,
            transactionFilter?.expenseCategoryId,
            transactionFilter?.incomeCategoryId,
            transactionFilter?.period,
            transactionFilter?.startDate,
            transactionFilter?.endDate,
            transactionFilter?.isRecurring
        )
    }
}