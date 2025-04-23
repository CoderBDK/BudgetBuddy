package com.coderbdk.budgetbuddy.data.repository.impl

import com.coderbdk.budgetbuddy.data.db.dao.IncomeCategoryDao
import com.coderbdk.budgetbuddy.data.db.entity.IncomeCategory
import com.coderbdk.budgetbuddy.data.repository.IncomeCategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultIncomeCategoryRepository @Inject constructor(
    private val incomeCategoryDao: IncomeCategoryDao
) : IncomeCategoryRepository {
    override suspend fun insertIncomeCategory(category: IncomeCategory): Long {
        return incomeCategoryDao.insert(category)
    }

    override suspend fun insertDefaultIncomeCategories(categories: List<IncomeCategory>) {
        incomeCategoryDao.insertAll(categories)
    }

    override suspend fun getDefaultIncomeCategories(): List<IncomeCategory> {
        return incomeCategoryDao.getDefaultCategories()
    }

    override fun getAllIncomeCategories(): Flow<List<IncomeCategory>> {
        return incomeCategoryDao.getIncomeCategories()
    }

    override suspend fun updateIncomeCategory(category: IncomeCategory) {

    }

    override suspend fun deleteIncomeCategory(categoryId: Int) {

    }
}