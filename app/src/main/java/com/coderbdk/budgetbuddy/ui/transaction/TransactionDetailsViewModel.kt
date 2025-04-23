package com.coderbdk.budgetbuddy.ui.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coderbdk.budgetbuddy.data.db.entity.ExpenseCategory
import com.coderbdk.budgetbuddy.data.db.entity.IncomeCategory
import com.coderbdk.budgetbuddy.domain.usecase.transaction.GetAllExpenseCategoriesUseCase
import com.coderbdk.budgetbuddy.domain.usecase.transaction.GetAllIncomeCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TransactionDetailsViewModel @Inject constructor(
    getAllExpenseCategoriesUseCase: GetAllExpenseCategoriesUseCase,
    getAllIncomeCategoriesUseCase: GetAllIncomeCategoriesUseCase
) : ViewModel() {

    val expenseCategories: StateFlow<List<ExpenseCategory>> = getAllExpenseCategoriesUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    val incomeCategories: StateFlow<List<IncomeCategory>> = getAllIncomeCategoriesUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
}