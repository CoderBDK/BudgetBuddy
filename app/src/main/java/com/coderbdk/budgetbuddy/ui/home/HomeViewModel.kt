package com.coderbdk.budgetbuddy.ui.home

import androidx.lifecycle.ViewModel
import com.coderbdk.budgetbuddy.data.model.TransactionType
import com.coderbdk.budgetbuddy.domain.budget.usecase.GetBudgetsUseCase
import com.coderbdk.budgetbuddy.domain.transaction.usecase.GetRecentTransactionsUseCase
import com.coderbdk.budgetbuddy.domain.transaction.usecase.GetTotalTransactionAmountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getBudgetsUseCase: GetBudgetsUseCase,
    getRecentTransactionsUseCase: GetRecentTransactionsUseCase,
    getTotalTransactionAmountUseCase: GetTotalTransactionAmountUseCase,

    ) : ViewModel() {

    val budgetsFlow = getBudgetsUseCase.invoke()
    val recentTransactionsFlow = getRecentTransactionsUseCase.invoke(13)
    val totalExpense = getTotalTransactionAmountUseCase.invoke(TransactionType.EXPENSE)
    val totalIncome = getTotalTransactionAmountUseCase.invoke(TransactionType.INCOME)
}