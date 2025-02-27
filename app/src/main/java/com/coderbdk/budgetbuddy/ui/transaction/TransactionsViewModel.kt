package com.coderbdk.budgetbuddy.ui.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.coderbdk.budgetbuddy.data.db.entity.Transaction
import com.coderbdk.budgetbuddy.domain.transaction.usecase.GetPaginatedTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    getPaginatedTransactionsUseCase: GetPaginatedTransactionsUseCase
) : ViewModel() {
    val transactions: Flow<PagingData<Transaction>> = getPaginatedTransactionsUseCase.invoke()
        .cachedIn(viewModelScope)
}