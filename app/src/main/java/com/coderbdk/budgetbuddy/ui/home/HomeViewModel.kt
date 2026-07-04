package com.coderbdk.budgetbuddy.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coderbdk.budgetbuddy.data.model.BudgetWithCategory
import com.coderbdk.budgetbuddy.data.model.TransactionType
import com.coderbdk.budgetbuddy.data.model.TransactionWithBothCategories
import com.coderbdk.budgetbuddy.domain.usecase.budget.GetBudgetsWithExpenseCategoryUseCase
import com.coderbdk.budgetbuddy.domain.usecase.init.InitCategoryUseCase
import com.coderbdk.budgetbuddy.domain.usecase.transaction.GetRecentTransactionsWithBothCategoriesUseCase
import com.coderbdk.budgetbuddy.domain.usecase.transaction.GetTotalTransactionAmountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val budgets: List<BudgetWithCategory> = emptyList(),
    val recentTransactions: List<TransactionWithBothCategories> = emptyList(),
    val totalExpense: Double = 0.0,
    val totalIncome: Double = 0.0,
    val isLoading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    getBudgetsWithExpenseCategoryUseCase: GetBudgetsWithExpenseCategoryUseCase,
    getRecentTransactionsWithBothCategoriesUseCase: GetRecentTransactionsWithBothCategoriesUseCase,
    getTotalTransactionAmountUseCase: GetTotalTransactionAmountUseCase,
    private val initCategoryUseCase: InitCategoryUseCase
) : ViewModel() {

    private val budgetsFlow = getBudgetsWithExpenseCategoryUseCase()
    private val recentTransactionsFlow = getRecentTransactionsWithBothCategoriesUseCase(15)
    private val totalExpenseFlow = getTotalTransactionAmountUseCase(TransactionType.EXPENSE)
    private val totalIncomeFlow = getTotalTransactionAmountUseCase(TransactionType.INCOME)

    val uiState: StateFlow<HomeUiState> = combine(
        budgetsFlow,
        recentTransactionsFlow,
        totalExpenseFlow,
        totalIncomeFlow
    ) { budgets, recentTransactions, expense, income ->
        HomeUiState(
            budgets = budgets,
            recentTransactions = recentTransactions,
            totalExpense = expense,
            totalIncome = income,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState(isLoading = true)
    )

    init {
        viewModelScope.launch {
            initCategoryUseCase()
        }
    }

}