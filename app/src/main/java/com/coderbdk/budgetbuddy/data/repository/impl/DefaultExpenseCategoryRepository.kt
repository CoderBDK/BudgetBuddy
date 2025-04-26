package com.coderbdk.budgetbuddy.data.repository.impl

import com.coderbdk.budgetbuddy.data.db.dao.ExpenseCategoryDao
import com.coderbdk.budgetbuddy.data.db.entity.ExpenseCategory
import com.coderbdk.budgetbuddy.data.repository.ExpenseCategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultExpenseCategoryRepository @Inject constructor(
    private val expenseCategoryDao: ExpenseCategoryDao
) : ExpenseCategoryRepository {
    override suspend fun insertExpenseCategory(category: ExpenseCategory): Long {
        return expenseCategoryDao.insert(category)
    }

    override suspend fun insertDefaultExpenseCategories(categories: List<ExpenseCategory>) {
        expenseCategoryDao.insertAll(categories)
    }

    override suspend fun getDefaultExpenseCategories(): List<ExpenseCategory> {
        return expenseCategoryDao.getDefaultCategories()
    }

    override fun getAllExpenseCategories(): Flow<List<ExpenseCategory>> {
        return expenseCategoryDao.getExpenseCategories()
    }

    override suspend fun updateExpenseCategory(category: ExpenseCategory) {

    }

    override suspend fun deleteExpenseCategory(categoryId: Int) {
        expenseCategoryDao.deleteExpenseCategory(categoryId)
    }
}