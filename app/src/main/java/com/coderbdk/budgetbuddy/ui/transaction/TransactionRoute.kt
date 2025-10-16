package com.coderbdk.budgetbuddy.ui.transaction

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.coderbdk.budgetbuddy.data.model.TransactionWithBothCategories
import com.coderbdk.budgetbuddy.ui.main.FabAction
import com.coderbdk.budgetbuddy.ui.main.MainViewModel

@Composable
fun TransactionsRoute(
    onNavigateToTransactionDetails: (TransactionWithBothCategories) -> Unit,
    viewModel: TransactionViewModel = hiltViewModel(),
    mainViewModel: MainViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val transactions = viewModel.filteredTransactions.collectAsLazyPagingItems()
    val filter by viewModel.filter.collectAsStateWithLifecycle()
    val expenseCategoryList by viewModel.expenseCategories.collectAsState(initial = emptyList())
    val incomeCategoryList by viewModel.incomeCategories.collectAsState(initial = emptyList())
    val fabAction by mainViewModel.fabAction.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(fabAction) {
        if (fabAction is FabAction.PrintTransaction) {
            viewModel.export(context, transactions.itemSnapshotList.items)
            mainViewModel.clearAction()
        }
    }

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        viewModel.clearPdfContentUri()
    }
    LaunchedEffect(uiState.pdfContentUri) {
        uiState.pdfContentUri?.let { uri ->
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            exportLauncher.launch(Intent.createChooser(shareIntent, "Print Transaction"))
        }
    }

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
