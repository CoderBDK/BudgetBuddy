package com.coderbdk.budgetbuddy.data.repository.impl

import com.coderbdk.budgetbuddy.data.db.dao.BudgetDao
import com.coderbdk.budgetbuddy.data.db.entity.Budget
import com.coderbdk.budgetbuddy.data.model.BudgetCategory
import com.coderbdk.budgetbuddy.data.model.BudgetPeriod
import com.coderbdk.budgetbuddy.data.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultBudgetRepository @Inject constructor(
    private val budgetDao: BudgetDao
) : BudgetRepository {
    override suspend fun insertBudget(budget: Budget) {
        budgetDao.insertBudget(budget)
    }

    override suspend fun updateBudget(budget: Budget) {
        budgetDao.updateBudget(budget)
    }

    override suspend fun doesBudgetExist(category: BudgetCategory, period: BudgetPeriod): Boolean {
        return budgetDao.doesBudgetExist(category, period)
    }

    override suspend fun incrementSpentAmount(
        category: BudgetCategory,
        period: BudgetPeriod,
        amount: Double
    ) {
        budgetDao.incrementSpentAmount(category, period, amount)
    }

    override suspend fun decrementSpentAmount(
        category: BudgetCategory,
        period: BudgetPeriod,
        amount: Double
    ) {
        budgetDao.decrementSpentAmount(category, period, amount)
    }

    override suspend fun getBudgetByCategoryAndPeriod(
        category: BudgetCategory,
        period: BudgetPeriod
    ): Budget? {
        return budgetDao.getBudgetByCategoryAndPeriod(category, period)
    }

    override fun getBudgets(): Flow<List<Budget>> {
        return budgetDao.getBudgets()
    }

}