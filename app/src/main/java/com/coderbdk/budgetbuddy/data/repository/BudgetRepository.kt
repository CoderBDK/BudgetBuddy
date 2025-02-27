package com.coderbdk.budgetbuddy.data.repository

import com.coderbdk.budgetbuddy.data.db.entity.Budget
import com.coderbdk.budgetbuddy.data.model.BudgetCategory
import com.coderbdk.budgetbuddy.data.model.BudgetPeriod
import kotlinx.coroutines.flow.Flow


interface BudgetRepository {
    suspend fun insertBudget(budget: Budget)
    suspend fun updateBudget(budget: Budget)
    suspend fun doesBudgetExist(category: BudgetCategory, period: BudgetPeriod): Boolean
    suspend fun incrementSpentAmount(category: BudgetCategory, period: BudgetPeriod, amount: Double)
    suspend fun decrementSpentAmount(category: BudgetCategory, period: BudgetPeriod, amount: Double)
    suspend fun getBudgetByCategoryAndPeriod(
        category: BudgetCategory,
        period: BudgetPeriod
    ): Budget?

    fun getBudgets(): Flow<List<Budget>>
}