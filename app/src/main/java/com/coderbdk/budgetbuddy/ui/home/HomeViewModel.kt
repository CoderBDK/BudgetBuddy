package com.coderbdk.budgetbuddy.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import com.coderbdk.budgetbuddy.data.model.TransactionType
import com.coderbdk.budgetbuddy.domain.usecase.budget.GetBudgetsUseCase
import com.coderbdk.budgetbuddy.domain.usecase.budget.GetBudgetsWithExpenseCategoryUseCase
import com.coderbdk.budgetbuddy.domain.usecase.init.InitCategoryUseCase
import com.coderbdk.budgetbuddy.domain.usecase.transaction.GetRecentTransactionsUseCase
import com.coderbdk.budgetbuddy.domain.usecase.transaction.GetRecentTransactionsWithBothCategoriesUseCase
import com.coderbdk.budgetbuddy.domain.usecase.transaction.GetTotalTransactionAmountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
   getBudgetsWithExpenseCategoryUseCase: GetBudgetsWithExpenseCategoryUseCase,
    getRecentTransactionsUseCase: GetRecentTransactionsUseCase,
   getRecentTransactionsWithBothCategoriesUseCase: GetRecentTransactionsWithBothCategoriesUseCase,
    getTotalTransactionAmountUseCase: GetTotalTransactionAmountUseCase,
    private val initCategoryUseCase: InitCategoryUseCase

    ) : ViewModel() {

    val budgetsFlow = getBudgetsWithExpenseCategoryUseCase.invoke()
    val recentTransactionsFlow = getRecentTransactionsWithBothCategoriesUseCase(13)
    val totalExpense = getTotalTransactionAmountUseCase.invoke(TransactionType.EXPENSE)
    val totalIncome = getTotalTransactionAmountUseCase.invoke(TransactionType.INCOME)

    fun initCategory(context: Context) = initCategoryUseCase(context)
}