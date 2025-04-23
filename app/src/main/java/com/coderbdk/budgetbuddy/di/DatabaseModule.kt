package com.coderbdk.budgetbuddy.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.coderbdk.budgetbuddy.data.db.BudgetBuddyDatabase
import com.coderbdk.budgetbuddy.data.db.dao.BudgetDao
import com.coderbdk.budgetbuddy.data.db.dao.ExpenseCategoryDao
import com.coderbdk.budgetbuddy.data.db.dao.IncomeCategoryDao
import com.coderbdk.budgetbuddy.data.db.dao.TransactionDao
import com.coderbdk.budgetbuddy.data.db.entity.ExpenseCategory
import com.coderbdk.budgetbuddy.data.db.entity.IncomeCategory
import com.coderbdk.budgetbuddy.data.model.DefaultExpenseCategory
import com.coderbdk.budgetbuddy.data.model.DefaultIncomeCategory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): BudgetBuddyDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            BudgetBuddyDatabase::class.java,
            "budget_buddy"
        ).build()
    }
    @Provides
    fun provideBudgetDao(database: BudgetBuddyDatabase): BudgetDao {
        return database.budgetDao()
    }

    @Provides
    fun provideTransactionDao(database: BudgetBuddyDatabase): TransactionDao {
        return database.transactionDao()
    }

    @Provides
    fun provideExpenseCategoryDao(database: BudgetBuddyDatabase): ExpenseCategoryDao {
        return database.expenseCategoryDao()
    }

    @Provides
    fun provideIncomeCategoryDao(database: BudgetBuddyDatabase): IncomeCategoryDao {
        return database.incomeCategoryDao()
    }
}