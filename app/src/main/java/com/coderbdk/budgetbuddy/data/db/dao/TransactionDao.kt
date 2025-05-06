package com.coderbdk.budgetbuddy.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.coderbdk.budgetbuddy.data.db.entity.Transaction
import com.coderbdk.budgetbuddy.data.model.BudgetPeriod
import com.coderbdk.budgetbuddy.data.model.TransactionType
import com.coderbdk.budgetbuddy.data.model.TransactionWithBothCategories
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY transaction_date DESC")
    fun getTransactions(type: TransactionType): Flow<List<Transaction>>

    @Query("SELECT SUM(amount) FROM transactions WHERE expense_category_id = :categoryId")
    fun getTotalSpentByCategory(categoryId: Int): Flow<Double>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = :type")
    fun getTotalTransactionAmount(type: TransactionType): Flow<Double>

    @Query("SELECT * FROM transactions ORDER BY transaction_date DESC LIMIT :count")
    fun getRecentTransactions(count: Int): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY transaction_date DESC LIMIT :count")
    fun getRecentTransactionsByTypeWithCategories(count: Int, type: TransactionType): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions ORDER BY transaction_date DESC")
    fun getPagedTransactions(): PagingSource<Int, Transaction>

    @Query("""
        SELECT t.*, ec.*, ic.*
        FROM transactions t
        LEFT JOIN expense_categories ec ON t.expense_category_id = ec.id
        LEFT JOIN income_categories ic ON t.income_category_id = ic.id
        WHERE t.type = :type
        ORDER BY t.transaction_date DESC
    """)
    fun getTransactionsWithBothCategories(type: TransactionType): Flow<List<TransactionWithBothCategories>>

    @Query("""
        SELECT
            t.*,
            ec.id AS expenseCategory_id,
            ec.name AS expenseCategory_name,
            ec.description AS expenseCategory_description,
            ec.color_code AS expenseCategory_color_code,
            ec.isDefault AS expenseCategory_isDefault,
            ic.id AS incomeCategory_id,
            ic.name AS incomeCategory_name,
            ic.description AS incomeCategory_description,
            ic.color_code AS incomeCategory_color_code,
            ic.isDefault AS incomeCategory_isDefault
        FROM transactions t
        LEFT JOIN expense_categories ec ON t.expense_category_id = ec.id
        LEFT JOIN income_categories ic ON t.income_category_id = ic.id
        ORDER BY t.transaction_date DESC
        LIMIT :count
    """)
    fun getRecentTransactionsWithBothCategories(count: Int): Flow<List<TransactionWithBothCategories>>

    @Query("""
        SELECT
            t.*,
            ec.id AS expenseCategory_id,
            ec.name AS expenseCategory_name,
            ec.description AS expenseCategory_description,
            ec.color_code AS expenseCategory_color_code,
            ec.isDefault AS expenseCategory_isDefault,
            ic.id AS incomeCategory_id,
            ic.name AS incomeCategory_name,
            ic.description AS incomeCategory_description,
            ic.color_code AS incomeCategory_color_code,
            ic.isDefault AS incomeCategory_isDefault
        FROM transactions t
        LEFT JOIN expense_categories ec ON t.expense_category_id = ec.id
        LEFT JOIN income_categories ic ON t.income_category_id = ic.id

        WHERE (:query IS NULL OR amount LIKE '%' || :query || '%') 
        AND (:type IS NULL OR type = :type) 
        AND (:expenseCategoryId IS NULL OR expense_category_id = :expenseCategoryId)
        AND (:incomeCategoryId IS NULL OR income_category_id = :incomeCategoryId)
        AND (:period IS NULL OR period = :period)
        AND (:startDate IS NULL OR transaction_date >= :startDate)
        AND (:endDate IS NULL OR transaction_date <= :endDate)
        AND (:isRecurring IS NULL OR is_recurring = :isRecurring)
        ORDER BY t.transaction_date DESC
    """)
    fun getFilteredTransactionsWithBothCategoriesPaging(
        query: String?,
        type: TransactionType?,
        expenseCategoryId: Int?,
        incomeCategoryId: Int?,
        period: BudgetPeriod?,
        startDate: Long?,
        endDate: Long?,
        isRecurring: Boolean?
    ): PagingSource<Int, TransactionWithBothCategories>

    @Query(
        """
        SELECT * FROM transactions
        WHERE (:query IS NULL OR amount LIKE '%' || :query || '%') 
        AND (:type IS NULL OR type = :type) 
        AND (:expenseCategoryId IS NULL OR expense_category_id = :expenseCategoryId)
        AND (:incomeCategoryId IS NULL OR income_category_id = :incomeCategoryId)
        AND (:period IS NULL OR period = :period)
        AND (:startDate IS NULL OR transaction_date >= :startDate)
        AND (:endDate IS NULL OR transaction_date <= :endDate)
        AND (:isRecurring IS NULL OR is_recurring = :isRecurring)
        ORDER BY transaction_date DESC
        """
    )
    fun getFilteredTransactionsPaging(
        query: String?,
        type: TransactionType?,
        expenseCategoryId: Int?,
        incomeCategoryId: Int?,
        period: BudgetPeriod?,
        startDate: Long?,
        endDate: Long?,
        isRecurring: Boolean?
    ): PagingSource<Int, Transaction>


    @Query("""
        SELECT
            t.*,
            ec.id AS expenseCategory_id,
            ec.name AS expenseCategory_name,
            ec.description AS expenseCategory_description,
            ec.color_code AS expenseCategory_color_code,
            ec.isDefault AS expenseCategory_isDefault,
            ic.id AS incomeCategory_id,
            ic.name AS incomeCategory_name,
            ic.description AS incomeCategory_description,
            ic.color_code AS incomeCategory_color_code,
            ic.isDefault AS incomeCategory_isDefault
        FROM transactions t
        LEFT JOIN expense_categories ec ON t.expense_category_id = ec.id
        LEFT JOIN income_categories ic ON t.income_category_id = ic.id

        WHERE (:query IS NULL OR amount LIKE '%' || :query || '%') 
        AND (:type IS NULL OR type = :type) 
        AND (:expenseCategoryId IS NULL OR expense_category_id = :expenseCategoryId)
        AND (:incomeCategoryId IS NULL OR income_category_id = :incomeCategoryId)
        AND (:period IS NULL OR period = :period)
        AND (:startDate IS NULL OR transaction_date >= :startDate)
        AND (:endDate IS NULL OR transaction_date <= :endDate)
        AND (:isRecurring IS NULL OR is_recurring = :isRecurring)
        ORDER BY t.transaction_date DESC
    """)
    fun getFilteredTransactionsWithBothCategories(
        query: String?,
        type: TransactionType?,
        expenseCategoryId: Int?,
        incomeCategoryId: Int?,
        period: BudgetPeriod?,
        startDate: Long?,
        endDate: Long?,
        isRecurring: Boolean?
    ): Flow<List<TransactionWithBothCategories>>

}
