package com.coderbdk.budgetbuddy.ui.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coderbdk.budgetbuddy.data.db.entity.ExpenseCategory
import com.coderbdk.budgetbuddy.data.db.entity.IncomeCategory
import com.coderbdk.budgetbuddy.data.model.BudgetFilter
import com.coderbdk.budgetbuddy.data.model.BudgetWithCategory
import com.coderbdk.budgetbuddy.data.model.TransactionFilter
import com.coderbdk.budgetbuddy.data.model.TransactionWithBothCategories
import com.coderbdk.budgetbuddy.domain.usecase.budget.GetBudgetsWithExpenseCategoryUseCase
import com.coderbdk.budgetbuddy.domain.usecase.budget.GetFilteredBudgetsWithCategoryUseCase
import com.coderbdk.budgetbuddy.domain.usecase.transaction.GetAllExpenseCategoriesUseCase
import com.coderbdk.budgetbuddy.domain.usecase.transaction.GetAllIncomeCategoriesUseCase
import com.coderbdk.budgetbuddy.domain.usecase.transaction.GetFilteredTransactionsWithBothCategoriesUseCase
import com.coderbdk.budgetbuddy.domain.usecase.transaction.GetRecentTransactionsWithBothCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject


data class AnalyticsUiState(
    val budgetFilter: BudgetFilter? = null,
    val transactionFilter: TransactionFilter? = null
)

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    getBudgetsWithExpenseCategoryUseCase: GetBudgetsWithExpenseCategoryUseCase,
    getTransactionWithBothCategoryUseCase: GetRecentTransactionsWithBothCategoriesUseCase,
    getFilteredBudgetsWithCategoryUseCase: GetFilteredBudgetsWithCategoryUseCase,
    getFilteredTransactionsWithBothCategoriesUseCase: GetFilteredTransactionsWithBothCategoriesUseCase,
    getAllExpenseCategoriesUseCase: GetAllExpenseCategoriesUseCase,
    getAllIncomeCategoriesUseCase: GetAllIncomeCategoriesUseCase
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

    val expenseCategories: StateFlow<List<ExpenseCategory>> =
        getAllExpenseCategoriesUseCase().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val incomeCategories: StateFlow<List<IncomeCategory>> = getAllIncomeCategoriesUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _uiState = MutableStateFlow(AnalyticsUiState())
    val uiState: StateFlow<AnalyticsUiState> = _uiState.asStateFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val filteredBudgets: Flow<List<BudgetWithCategory>> = uiState
        .debounce(300)
        .flatMapLatest {
            getFilteredBudgetsWithCategoryUseCase(
                it.budgetFilter
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val filteredTransactions: Flow<List<TransactionWithBothCategories>> = uiState
        .debounce(300)
        .flatMapLatest {
            getFilteredTransactionsWithBothCategoriesUseCase(
                it.transactionFilter
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onBudgetFilter(budgetFilter: BudgetFilter?) {
        _uiState.update {
            it.copy(budgetFilter = budgetFilter)
        }
    }

    fun onTransactionFilter(transactionFilter: TransactionFilter?) {
        _uiState.update {
            it.copy(transactionFilter = transactionFilter)
        }
    }

}