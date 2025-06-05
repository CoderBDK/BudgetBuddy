package com.coderbdk.budgetbuddy.ui.category

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coderbdk.budgetbuddy.data.model.TransactionType

@Composable
fun CategoryManageRoute(
    type: TransactionType,
    viewModel: CategoryManageViewModel = hiltViewModel(),
) {
    val expenseCategoryList by viewModel.expenseCategories.collectAsState(initial = emptyList())
    val incomeCategoryList by viewModel.incomeCategories.collectAsState(initial = emptyList())
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CategoryManageScreen(
        type = type,
        expenseCategoryList = expenseCategoryList,
        incomeCategoryList = incomeCategoryList,
        uiState = uiState,
        insertExpenseCategory = viewModel::insertExpenseCategory,
        insertIncomeCategory = viewModel::insertIncomeCategory,
        deleteExpenseCategoryById = viewModel::deleteExpenseCategoryById,
        showCreateCategoryDialog = viewModel::showCreateCategoryDialog,
        hideCreateCategoryDialog = viewModel::hideCreateCategoryDialog
    )
}