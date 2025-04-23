package com.coderbdk.budgetbuddy.data.repository

import com.coderbdk.budgetbuddy.data.db.entity.IncomeCategory
import kotlinx.coroutines.flow.Flow

interface IncomeCategoryRepository {
    suspend fun insertIncomeCategory(category: IncomeCategory): Long
    suspend fun insertDefaultIncomeCategories(categories: List<IncomeCategory>)
    suspend fun getDefaultIncomeCategories(): List<IncomeCategory>
    fun getAllIncomeCategories(): Flow<List<IncomeCategory>>
    suspend fun updateIncomeCategory(category: IncomeCategory)
    suspend fun deleteIncomeCategory(categoryId: Int)
}
