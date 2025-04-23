package com.coderbdk.budgetbuddy.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.coderbdk.budgetbuddy.data.db.entity.IncomeCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomeCategoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(category: IncomeCategory): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(categories: List<IncomeCategory>)

    @Query("SELECT * FROM income_categories WHERE isDefault = 1")
    suspend fun getDefaultCategories(): List<IncomeCategory>

    @Query("SELECT * FROM income_categories")
    fun getIncomeCategories(): Flow<List<IncomeCategory>>
}