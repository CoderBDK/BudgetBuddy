package com.coderbdk.budgetbuddy.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.coderbdk.budgetbuddy.data.db.entity.Budget
import com.coderbdk.budgetbuddy.data.db.entity.ExpenseCategory
import com.coderbdk.budgetbuddy.data.model.BudgetFilter
import com.coderbdk.budgetbuddy.data.model.BudgetPeriod
import com.coderbdk.budgetbuddy.data.model.BudgetWithCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: Budget)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateBudget(budget: Budget)

    @Query("SELECT EXISTS(SELECT 1 FROM budgets WHERE expense_category_id = :categoryId AND period = :period LIMIT 1)")
    suspend fun doesBudgetExist(categoryId: Int?, period: BudgetPeriod?): Boolean

    @Query("UPDATE budgets SET spent_amount = spent_amount + :amount WHERE expense_category_id = :categoryId AND period = :period")
    suspend fun incrementSpentAmount(categoryId: Int, period: BudgetPeriod, amount: Double)

    @Query("UPDATE budgets SET spent_amount = spent_amount - :amount WHERE expense_category_id = :categoryId AND period = :period")
    suspend fun decrementSpentAmount(categoryId: Int, period: BudgetPeriod, amount: Double)

    @Query("SELECT * FROM budgets WHERE expense_category_id = :categoryId AND period = :period LIMIT 1")
    suspend fun getBudgetByCategoryAndPeriod(
        categoryId: Int,
        period: BudgetPeriod
    ): Budget?

    @Query("SELECT * FROM budgets")
    fun getBudgets(): Flow<List<Budget>>

    @Transaction
    @Query("SELECT * FROM budgets")
    fun getBudgetsWithCategory(): Flow<List<BudgetWithCategory>>

    @Transaction
    @Query(
        """
    SELECT * FROM budgets
    WHERE (:query IS NULL OR limit_amount LIKE '%' || :query || '%')
    AND (:expenseCategoryId IS NULL OR expense_category_id = :expenseCategoryId)
    AND (:period IS NULL OR period = :period)
    AND (:startDate IS NULL OR start_date >= :startDate)
    AND (:endDate IS NULL OR end_date <= :endDate)
    """
    )
    fun getFilteredBudgetsWithCategory(
        query: String?,
        expenseCategoryId: Int?,
        period: BudgetPeriod?,
        startDate: Long?,
        endDate: Long?,
    ): Flow<List<BudgetWithCategory>>


}
