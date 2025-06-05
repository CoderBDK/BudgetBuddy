package com.coderbdk.budgetbuddy.ui.transaction

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.coderbdk.budgetbuddy.data.model.TransactionWithBothCategories

@Composable
fun TransactionsRoute(
    onNavigateToTransactionDetails: (TransactionWithBothCategories) -> Unit,
    viewModel: TransactionsViewModel = hiltViewModel()
) {
    val transactions = viewModel.filteredTransactions.collectAsLazyPagingItems()
    val filter by viewModel.filter.collectAsState()
    val expenseCategoryList by viewModel.expenseCategories.collectAsState(initial = emptyList())
    val incomeCategoryList by viewModel.incomeCategories.collectAsState(initial = emptyList())

    TransactionsScreen(
        transactions = transactions,
        expenseCategoryList = expenseCategoryList,
        incomeCategoryList = incomeCategoryList,
        filter = filter,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onFilterChange = viewModel::onFilterChange,
        onNavigateToTransactionDetails = onNavigateToTransactionDetails
    )
}
