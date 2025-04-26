package com.coderbdk.budgetbuddy.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.coderbdk.budgetbuddy.data.db.entity.ExpenseCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseCategoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(category: ExpenseCategory): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(categories: List<ExpenseCategory>)

    @Query("DELETE FROM expense_categories WHERE id =:categoryId")
    suspend fun deleteExpenseCategory(categoryId: Int)

    @Query("SELECT * FROM expense_categories WHERE isDefault = 1")
    suspend fun getDefaultCategories(): List<ExpenseCategory>

    @Query("SELECT * FROM expense_categories")
    fun getExpenseCategories(): Flow<List<ExpenseCategory>>
}