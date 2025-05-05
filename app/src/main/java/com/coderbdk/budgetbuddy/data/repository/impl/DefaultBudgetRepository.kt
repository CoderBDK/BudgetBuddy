package com.coderbdk.budgetbuddy.data.repository.impl

import com.coderbdk.budgetbuddy.data.db.dao.BudgetDao
import com.coderbdk.budgetbuddy.data.db.entity.Budget
import com.coderbdk.budgetbuddy.data.model.BudgetFilter
import com.coderbdk.budgetbuddy.data.model.BudgetPeriod
import com.coderbdk.budgetbuddy.data.model.BudgetWithCategory
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

    override suspend fun doesBudgetExist(categoryId: Int, period: BudgetPeriod): Boolean {
        return budgetDao.doesBudgetExist(categoryId, period)
    }

    override suspend fun incrementSpentAmount(
        categoryId: Int,
        period: BudgetPeriod,
        amount: Double
    ) {
        budgetDao.incrementSpentAmount(categoryId, period, amount)
    }

    override suspend fun decrementSpentAmount(
        categoryId: Int,
        period: BudgetPeriod,
        amount: Double
    ) {
        budgetDao.decrementSpentAmount(categoryId, period, amount)
    }

    override suspend fun getBudgetByCategoryAndPeriod(
        categoryId: Int,
        period: BudgetPeriod
    ): Budget? {
        return budgetDao.getBudgetByCategoryAndPeriod(categoryId, period)
    }

    override fun getBudgets(): Flow<List<Budget>> {
        return budgetDao.getBudgets()
    }

    override fun getBudgetsWithCategory(): Flow<List<BudgetWithCategory>> {
        return budgetDao.getBudgetsWithCategory()
    }

    override fun getFilteredBudgetsWithCategory(budgetFilter: BudgetFilter?): Flow<List<BudgetWithCategory>> {
        return budgetDao.getFilteredBudgetsWithCategory(
            budgetFilter?.query,
            budgetFilter?.expenseCategoryId,
            budgetFilter?.period,
            budgetFilter?.startDate,
            budgetFilter?.endDate
        )
    }
}