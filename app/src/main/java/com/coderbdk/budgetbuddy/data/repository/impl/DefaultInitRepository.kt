package com.coderbdk.budgetbuddy.data.repository.impl

import android.content.Context
import com.coderbdk.budgetbuddy.data.db.dao.ExpenseCategoryDao
import com.coderbdk.budgetbuddy.data.db.dao.IncomeCategoryDao
import com.coderbdk.budgetbuddy.data.db.entity.ExpenseCategory
import com.coderbdk.budgetbuddy.data.db.entity.IncomeCategory
import com.coderbdk.budgetbuddy.data.model.DefaultExpenseCategory
import com.coderbdk.budgetbuddy.data.model.DefaultIncomeCategory
import com.coderbdk.budgetbuddy.data.repository.InitRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class DefaultInitRepository @Inject constructor(
    private val expenseCategoryDao: ExpenseCategoryDao,
    private val incomeCategoryDao: IncomeCategoryDao
) : InitRepository {
    override fun initCategory(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {

            if (expenseCategoryDao.getDefaultCategories().isEmpty()) {
                val defaultExpenseCategories =
                    DefaultExpenseCategory.entries.map { categoryEnum ->
                        ExpenseCategory(
                            name = categoryEnum.name,
                            description = categoryEnum.description,
                            colorCode = categoryEnum.getColor(context),
                            isDefault = true
                        )
                    }
                expenseCategoryDao.insertAll(defaultExpenseCategories)
            }

            if (incomeCategoryDao.getDefaultCategories().isEmpty()) {
                val defaultIncomeCategories =
                    DefaultIncomeCategory.entries.map { categoryEnum ->
                        IncomeCategory(
                            name = categoryEnum.name,
                            description = categoryEnum.description,
                            colorCode = categoryEnum.getColor(context),
                            isDefault = true
                        )
                    }
                incomeCategoryDao.insertAll(defaultIncomeCategories)
            }
        }
    }
}