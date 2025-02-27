package com.coderbdk.budgetbuddy.di

import com.coderbdk.budgetbuddy.data.db.dao.BudgetDao
import com.coderbdk.budgetbuddy.data.db.dao.TransactionDao
import com.coderbdk.budgetbuddy.data.repository.BudgetRepository
import com.coderbdk.budgetbuddy.data.repository.TransactionRepository
import com.coderbdk.budgetbuddy.data.repository.impl.DefaultBudgetRepository
import com.coderbdk.budgetbuddy.data.repository.impl.DefaultTransactionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {
    @Provides
    fun provideBudgetRepository(
        dao: BudgetDao
    ): BudgetRepository {
        return DefaultBudgetRepository(dao)
    }

    @Provides
    fun provideTransactionRepository(
        dao: TransactionDao
    ): TransactionRepository {
        return DefaultTransactionRepository(dao)
    }

}