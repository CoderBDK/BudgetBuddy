package com.coderbdk.budgetbuddy.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.coderbdk.budgetbuddy.data.db.dao.BudgetDao
import com.coderbdk.budgetbuddy.data.db.dao.TransactionDao
import com.coderbdk.budgetbuddy.data.db.entity.Budget
import com.coderbdk.budgetbuddy.data.db.entity.Transaction

@Database(
    entities = [Budget::class, Transaction::class],
    version = 1
)
abstract class BudgetBuddyDatabase : RoomDatabase() {
   abstract fun budgetDao(): BudgetDao
   abstract fun transactionDao(): TransactionDao
}