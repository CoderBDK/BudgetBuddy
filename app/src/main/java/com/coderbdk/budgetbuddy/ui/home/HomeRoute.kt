package com.coderbdk.budgetbuddy.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@Composable
fun HomeScreenRoute(
    onNavigateToAddTransaction: () -> Unit,
    onNavigateToBudgets: () -> Unit,
    onNavigateToTransactions: () -> Unit,
) {
    val viewModel = hiltViewModel<HomeViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        uiState = uiState,
        onNavigateToAddTransaction = onNavigateToAddTransaction,
        onNavigateToBudgets = onNavigateToBudgets,
        onNavigateToTransactions = onNavigateToTransactions,
    )
}