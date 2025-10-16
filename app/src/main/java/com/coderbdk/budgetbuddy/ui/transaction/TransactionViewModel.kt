package com.coderbdk.budgetbuddy.ui.transaction

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.coderbdk.budgetbuddy.data.db.entity.ExpenseCategory
import com.coderbdk.budgetbuddy.data.db.entity.IncomeCategory
import com.coderbdk.budgetbuddy.data.model.TransactionFilter
import com.coderbdk.budgetbuddy.data.model.TransactionWithBothCategories
import com.coderbdk.budgetbuddy.domain.usecase.transaction.GetAllExpenseCategoriesUseCase
import com.coderbdk.budgetbuddy.domain.usecase.transaction.GetAllIncomeCategoriesUseCase
import com.coderbdk.budgetbuddy.domain.usecase.transaction.GetSearchWithFilteredTransactionsBothCategoriesUseCase
import com.coderbdk.budgetbuddy.utils.PdfExport
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.io.File
import javax.inject.Inject

data class TransactionsUiState(
    val isLoading: Boolean = false,
    val pdfContentUri: Uri? = null

)

@HiltViewModel
class TransactionViewModel @Inject constructor(
    getAllExpenseCategoriesUseCase: GetAllExpenseCategoriesUseCase,
    getAllIncomeCategoriesUseCase: GetAllIncomeCategoriesUseCase,
    getSearchWithFilteredTransactionsBothCategoriesUseCase: GetSearchWithFilteredTransactionsBothCategoriesUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(TransactionsUiState())
    val uiState: StateFlow<TransactionsUiState> = _uiState.asStateFlow()

    private val _filter = MutableStateFlow(TransactionFilter())
    val filter: StateFlow<TransactionFilter> = _filter.asStateFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val filteredTransactions: Flow<PagingData<TransactionWithBothCategories>> = filter
        .debounce(300)
        .flatMapLatest {
            getSearchWithFilteredTransactionsBothCategoriesUseCase(
                it.query,
                it.type,
                it.expenseCategoryId,
                it.incomeCategoryId,
                it.period,
                it.startDate,
                it.endDate,
                it.isRecurring
            )
        }
        .cachedIn(viewModelScope)

    val expenseCategories: StateFlow<List<ExpenseCategory>> =
        getAllExpenseCategoriesUseCase().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    val incomeCategories: StateFlow<List<IncomeCategory>> = getAllIncomeCategoriesUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onFilterChange(updatedFilter: TransactionFilter) {
        _filter.value = updatedFilter
    }

    fun onSearchQueryChange(query: String) {
        _filter.value = _filter.value.copy(query = query)
    }

    fun export(context: Context, transactions: List<TransactionWithBothCategories>) {
        val dir = File(context.filesDir, "transactions")
        if (!dir.exists()) dir.mkdir()
        val outputFile = File(dir, "transactions_report.pdf")
        val uri = FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName + ".fileprovider",
            outputFile
        )
        _uiState.update {
            it.copy(
                isLoading = true
            )
        }
        PdfExport.export(
            outputFile,
            transactions
        )
        _uiState.update {
            it.copy(
                pdfContentUri = uri,
                isLoading = false
            )
        }
    }

    fun clearPdfContentUri() {
        _uiState.update {
            it.copy(
                pdfContentUri = null,
                isLoading = false
            )
        }
    }
}