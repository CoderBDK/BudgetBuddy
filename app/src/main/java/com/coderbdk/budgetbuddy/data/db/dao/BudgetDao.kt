package com.coderbdk.budgetbuddy.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.coderbdk.budgetbuddy.data.db.entity.Budget
import com.coderbdk.budgetbuddy.data.model.BudgetCategory
import com.coderbdk.budgetbuddy.data.model.BudgetPeriod
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: Budget)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateBudget(budget: Budget)

    @Query("SELECT EXISTS(SELECT 1 FROM budgets WHERE category = :category AND period = :period LIMIT 1)")
    suspend fun doesBudgetExist(category: BudgetCategory, period: BudgetPeriod): Boolean

    @Query("UPDATE budgets SET spent_amount = spent_amount + :amount WHERE category = :category AND period = :period")
    suspend fun incrementSpentAmount(category: BudgetCategory, period: BudgetPeriod, amount: Double)

    @Query("UPDATE budgets SET spent_amount = spent_amount - :amount WHERE category = :category AND period = :period")
    suspend fun decrementSpentAmount(category: BudgetCategory, period: BudgetPeriod, amount: Double)

    @Query("SELECT * FROM budgets WHERE category = :category AND period = :period LIMIT 1")
    suspend fun getBudgetByCategoryAndPeriod(
        category: BudgetCategory,
        period: BudgetPeriod
    ): Budget?

    @Query("SELECT * FROM budgets")
    fun getBudgets(): Flow<List<Budget>>

}
