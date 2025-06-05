package com.coderbdk.budgetbuddy.ui.analytics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.coderbdk.budgetbuddy.data.model.TransactionType

@Composable
fun AnalyticsRoute(
    onNavigateToCategoryManage: (TransactionType) -> Unit,
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val budgets by viewModel.filteredBudgets.collectAsStateWithLifecycle(emptyList())
    val transactions by viewModel.filteredTransactions.collectAsStateWithLifecycle(emptyList())
    val expenseCategories by viewModel.expenseCategories.collectAsStateWithLifecycle()
    val incomeCategoryList by viewModel.incomeCategories.collectAsState(initial = emptyList())

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AnalyticsScreen(
        uiState = uiState,
        budgets = budgets,
        transactions = transactions,
        expenseCategoryList = expenseCategories,
        incomeCategoryList = incomeCategoryList,
        onBudgetFilter = viewModel::onBudgetFilter,
        onTransactionFilter = viewModel::onTransactionFilter,
        onNavigateToCategoryManage = onNavigateToCategoryManage
    )
}
