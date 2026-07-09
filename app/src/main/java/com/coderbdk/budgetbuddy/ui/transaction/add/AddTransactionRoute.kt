package com.coderbdk.budgetbuddy.ui.transaction.add

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
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
            onDismissRequest = { viewModel.onEvent(TransactionUiEvent.DismissBudgetDialog) },
            onNavigateToBudgets = onNavigateToBudgets
        )
    }
    if (expenseCategoryList.isEmpty()) return

    AddTransactionScreen(
        expenseCategoryList = expenseCategoryList,
        incomeCategoryList = incomeCategoryList,
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateToCategoryManage = onNavigateToCategoryManage
    )
}