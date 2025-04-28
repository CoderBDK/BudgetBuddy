package com.coderbdk.budgetbuddy.ui.analysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coderbdk.budgetbuddy.data.model.TransactionWithBothCategories
import com.coderbdk.budgetbuddy.domain.usecase.budget.GetBudgetsWithExpenseCategoryUseCase
import com.coderbdk.budgetbuddy.domain.usecase.transaction.GetRecentTransactionsWithBothCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    getBudgetsWithExpenseCategoryUseCase: GetBudgetsWithExpenseCategoryUseCase,
    getTransactionWithBothCategoryUseCase: GetRecentTransactionsWithBothCategoriesUseCase
) : ViewModel() {
    val budgetsFlow = getBudgetsWithExpenseCategoryUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    val transactionsFlow = getTransactionWithBothCategoryUseCase(100).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
}