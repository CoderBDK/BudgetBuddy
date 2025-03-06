package com.coderbdk.budgetbuddy.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.coderbdk.budgetbuddy.data.db.entity.Transaction
import com.coderbdk.budgetbuddy.data.model.BudgetCategory
import com.coderbdk.budgetbuddy.data.model.BudgetPeriod
import com.coderbdk.budgetbuddy.data.model.TransactionType
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY date DESC")
    fun getTransactions(type: TransactionType): Flow<List<Transaction>>

    @Query("SELECT SUM(amount) FROM transactions WHERE category = :category")
    fun getTotalSpentByCategory(category: String): Flow<Double>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = :type")
    fun getTotalTransactionAmount(type: TransactionType): Flow<Double>

    @Query("SELECT * FROM transactions ORDER BY date DESC LIMIT :count")
    fun getRecentTransactions(count: Int): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getPagedTransactions(): PagingSource<Int, Transaction>

    @Query(
        """
        SELECT * FROM transactions
        WHERE (:query IS NULL OR amount LIKE '%' || :query || '%') 
        AND (:type IS NULL OR type = :type) 
        AND (:category IS NULL OR category = :category)
        AND (:period IS NULL OR period = :period)
        AND (:startDate IS NULL OR date >= :startDate)
        AND (:endDate IS NULL OR date <= :endDate)
        AND (:isRecurring IS NULL OR is_recurring = :isRecurring)
        ORDER BY date DESC
        """
    )
    fun getFilteredTransactionsPaging(
        query: String?,
        type: TransactionType?,
        category: BudgetCategory?,
        period: BudgetPeriod?,
        startDate: Long?,
        endDate: Long?,
        isRecurring: Boolean?
    ): PagingSource<Int, Transaction>
}
