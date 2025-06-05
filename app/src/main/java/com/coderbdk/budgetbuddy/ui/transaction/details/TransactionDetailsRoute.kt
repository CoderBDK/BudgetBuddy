package com.coderbdk.budgetbuddy.ui.transaction.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.coderbdk.budgetbuddy.data.model.TransactionWithBothCategories

@Composable
fun TransactionDetailsRoute(
    transaction: TransactionWithBothCategories,
    viewModel: TransactionDetailsViewModel = hiltViewModel()
) {
    val expenseCategoryList by viewModel.expenseCategories.collectAsState(initial = emptyList())
    val incomeCategoryList by viewModel.incomeCategories.collectAsState(initial = emptyList())

    Column(
        Modifier.fillMaxSize()
    ) {
        TransactionDetailsContent(
            expenseCategoryList,
            incomeCategoryList,
            transaction.transaction
        )
    }
}