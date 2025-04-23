package com.coderbdk.budgetbuddy.data.repository

import com.coderbdk.budgetbuddy.data.db.entity.ExpenseCategory
import kotlinx.coroutines.flow.Flow

interface ExpenseCategoryRepository {
    suspend fun insertExpenseCategory(category: ExpenseCategory): Long
    suspend fun insertDefaultExpenseCategories(categories: List<ExpenseCategory>)
    suspend fun getDefaultExpenseCategories(): List<ExpenseCategory>
    fun getAllExpenseCategories(): Flow<List<ExpenseCategory>>
    suspend fun updateExpenseCategory(category: ExpenseCategory)
    suspend fun deleteExpenseCategory(categoryId: Int)
}