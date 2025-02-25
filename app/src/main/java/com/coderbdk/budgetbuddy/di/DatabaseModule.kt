package com.coderbdk.budgetbuddy.di

import android.content.Context
import androidx.room.Room
import com.coderbdk.budgetbuddy.data.db.BudgetBuddyDatabase
import com.coderbdk.budgetbuddy.data.db.dao.BudgetDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): BudgetBuddyDatabase {
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
}