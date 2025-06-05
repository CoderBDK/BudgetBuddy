package com.coderbdk.budgetbuddy.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeScreenRoute(
    onNavigateToAddTransaction: () -> Unit,
    onNavigateToBudgets: () -> Unit,
    onNavigateToTransactions: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    // val expenses by viewModel.expensesFlow.collectAsStateWithLifecycle(emptyList())
    // val incomes by viewModel.incomesFlow.collectAsStateWithLifecycle(emptyList())
    val budgets by viewModel.budgetsFlow.collectAsStateWithLifecycle(emptyList())
    val recentTransactions by viewModel.recentTransactionsFlow.collectAsStateWithLifecycle(emptyList())
    val totalExpense by viewModel.totalExpense.collectAsStateWithLifecycle(0.0)
    val totalIncome by viewModel.totalIncome.collectAsStateWithLifecycle(0.0)
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.initCategory(context)
    }
    HomeScreen(
        totalIncome = totalIncome,
        totalExpense = totalExpense,
        recentTransactions = recentTransactions,
        budgets = budgets,
        onNavigateToAddTransaction = onNavigateToAddTransaction,
        onNavigateToBudgets = onNavigateToBudgets,
        onNavigateToTransactions = onNavigateToTransactions,
    )
}