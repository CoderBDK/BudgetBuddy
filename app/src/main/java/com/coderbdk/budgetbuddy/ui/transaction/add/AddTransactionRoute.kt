package com.coderbdk.budgetbuddy.ui.transaction.add

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coderbdk.budgetbuddy.data.model.TransactionType
import com.coderbdk.budgetbuddy.ui.transaction.dialog.AlertDialogBudgetCreate


@Composable
fun AddTransactionRoute(
    onNavigateToBudgets: () -> Unit,
    onNavigateToCategoryManage: (TransactionType) -> Unit,
    viewModel: AddTransactionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val expenseCategoryList by viewModel.expenseCategories.collectAsState(initial = emptyList())
    val incomeCategoryList by viewModel.incomeCategories.collectAsState(initial = emptyList())

    if (uiState.isBudgetCreationRequired) {
        AlertDialogBudgetCreate(
            onDismissRequest = viewModel::hideBudgetCreation,
            onNavigateToBudgets = onNavigateToBudgets
        )
    }
    if (expenseCategoryList.isEmpty()) return

    AddTransactionScreen(
        expenseCategoryList = expenseCategoryList,
        incomeCategoryList = incomeCategoryList,
        uiState = uiState,
        uiEvent = TransactionUiEvent(
            onExpenseCategoryChange = viewModel::onExpenseCategoryChange,
            onIncomeCategoryChange = viewModel::onIncomeCategoryChange,
            onPeriodChange = viewModel::onPeriodChange,
            onRecurringChange = viewModel::onRecurringChange,
            onAmountChange = viewModel::onAmountChange,
            onTypeChange = viewModel::onTypeChange,
            onNotesChange = viewModel::onNotesChange,
            saveTransaction = viewModel::saveTransaction
        ),
        onNavigateToCategoryManage = onNavigateToCategoryManage
    )
}