package com.coderbdk.budgetbuddy.ui.budget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coderbdk.budgetbuddy.ui.main.MainViewModel

@Composable
fun BudgetRoute(viewModel: BudgetViewModel = hiltViewModel(), mainViewModel: MainViewModel) {

    val budgets by viewModel.budgetsFlow.collectAsStateWithLifecycle(emptyList())
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val fabAction by mainViewModel.fabAction.collectAsState()
    val expenseCategoryList by viewModel.expenseCategories.collectAsState(initial = emptyList())

    BudgetScreen(
        fabAction = fabAction,
        budgets = budgets,
        expenseCategoryList = expenseCategoryList,
        uiState = uiState,
        addBudget = viewModel::addBudget,
        updateBudget = viewModel::updateBudget,
        updateBudgetCancel = viewModel::updateBudgetCancel,
        clearFacAction = mainViewModel::clearAction
    )
}
