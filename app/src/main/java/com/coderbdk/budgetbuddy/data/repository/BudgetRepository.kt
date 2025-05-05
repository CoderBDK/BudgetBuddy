package com.coderbdk.budgetbuddy.data.repository

import com.coderbdk.budgetbuddy.data.db.entity.Budget
import com.coderbdk.budgetbuddy.data.db.entity.ExpenseCategory
import com.coderbdk.budgetbuddy.data.model.BudgetFilter
import com.coderbdk.budgetbuddy.data.model.BudgetPeriod
import com.coderbdk.budgetbuddy.data.model.BudgetWithCategory
import kotlinx.coroutines.flow.Flow


interface BudgetRepository {
    suspend fun insertBudget(budget: Budget)
    suspend fun updateBudget(budget: Budget)
    suspend fun doesBudgetExist(categoryId: Int, period: BudgetPeriod): Boolean
    suspend fun incrementSpentAmount(categoryId: Int, period: BudgetPeriod, amount: Double)
    suspend fun decrementSpentAmount(categoryId: Int, period: BudgetPeriod, amount: Double)
    suspend fun getBudgetByCategoryAndPeriod(
        categoryId: Int,
        period: BudgetPeriod
    ): Budget?

    fun getBudgets(): Flow<List<Budget>>
    fun getBudgetsWithCategory(): Flow<List<BudgetWithCategory>>
    fun getFilteredBudgetsWithCategory(
        budgetFilter: BudgetFilter?
    ): Flow<List<BudgetWithCategory>>
}