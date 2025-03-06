package com.coderbdk.budgetbuddy.ui.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.coderbdk.budgetbuddy.data.db.entity.Transaction
import com.coderbdk.budgetbuddy.data.model.TransactionFilter
import com.coderbdk.budgetbuddy.domain.transaction.usecase.GetSearchWithFilteredTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    getSearchWithFilteredTransactionsUseCase: GetSearchWithFilteredTransactionsUseCase
) : ViewModel() {
    private val _filter = MutableStateFlow(TransactionFilter())
    val filter: StateFlow<TransactionFilter> = _filter.asStateFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val filteredTransactions: Flow<PagingData<Transaction>> = filter
        .debounce(300)
        .flatMapLatest {
            getSearchWithFilteredTransactionsUseCase(
                it.query,
                it.type,
                it.category,
                it.period,
                it.startDate,
                it.endDate,
                it.isRecurring
            )
        }
        .cachedIn(viewModelScope)

    fun updateFilter(updatedFilter: TransactionFilter) {
        _filter.value = updatedFilter
    }

    fun setSearchQuery(query: String) {
        _filter.value = _filter.value.copy(query = query)
    }
}