package com.coderbdk.budgetbuddy.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.coderbdk.budgetbuddy.data.db.dao.BudgetDao
import com.coderbdk.budgetbuddy.data.db.entity.BudgetEntity

@Database(
    entities = [BudgetEntity::class],
    version = 1
)
abstract class BudgetBuddyDatabase : RoomDatabase() {
   abstract fun budgetDao(): BudgetDao
}