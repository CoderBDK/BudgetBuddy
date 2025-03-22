package com.coderbdk.budgetbuddy.ui.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coderbdk.budgetbuddy.data.db.entity.Transaction
import com.coderbdk.budgetbuddy.data.model.BudgetCategory
import com.coderbdk.budgetbuddy.data.model.BudgetPeriod
import com.coderbdk.budgetbuddy.data.model.TransactionType
import com.coderbdk.budgetbuddy.domain.budget.usecase.DoesBudgetExistUseCase
import com.coderbdk.budgetbuddy.domain.transaction.usecase.InsertTransactionWithBudgetIncrementUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class TransactionUiState(
    val type: TransactionType = TransactionType.EXPENSE,
    val category: BudgetCategory = BudgetCategory.FOOD,
    val period: BudgetPeriod = BudgetPeriod.DAILY,
    val notes: String = "",
    val amount: Double = 0.0,
    val isRecurring: Boolean = false,
    val isSaving: Boolean = false,
    val isBudgetCreationRequired: Boolean = false,
)

data class TransactionUiEvent(
    val onAmountChange: (String) -> Unit,
    val onCategoryChange: (BudgetCategory) -> Unit,
    val onPeriodChange: (BudgetPeriod) -> Unit,
    val onTypeChange: (TransactionType) -> Unit,
    val onRecurringChange: (Boolean) -> Unit,
    val onNotesChange: (String) -> Unit,
    val saveTransaction: () -> Unit
)

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val insertTransactionWithBudgetIncrementUseCase: InsertTransactionWithBudgetIncrementUseCase,
    private val doesBudgetExistUseCase: DoesBudgetExistUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionUiState())
    val uiState: StateFlow<TransactionUiState> = _uiState.asStateFlow()

    fun saveTransaction() {

        viewModelScope.launch {

            val transaction = _uiState.value.run {
                Transaction(
                    type = type,
                    amount = amount,
                    category = category,
                    period = period,
                    date = System.currentTimeMillis(),
                    notes = notes,
                    isRecurring = isRecurring
                )
            }

            if (transaction.type == TransactionType.EXPENSE) {
                if (!doesBudgetExistUseCase(
                        transaction.category!!,
                        transaction.period!!
                    )
                ) {
                    _uiState.update { it.copy(isBudgetCreationRequired = true) }
                    return@launch
                }
            }

            _uiState.update { it.copy(isSaving = true) }
            insertTransactionWithBudgetIncrementUseCase(transaction)
            delay(1000)
            _uiState.update { it.copy(isSaving = false) }
        }
    }

    fun onAmountChange(amount: String) {
        _uiState.update {
            it.copy(amount = amount.toDoubleOrNull() ?: 0.0)
        }
    }


    fun onCategoryChange(category: BudgetCategory) {
        _uiState.update {
            it.copy(
                category = category
            )
        }
    }

    fun onPeriodChange(value: BudgetPeriod) {
        _uiState.update {
            it.copy(
                period = value
            )
        }
    }

    fun onTypeChange(value: TransactionType) {
        _uiState.update {
            it.copy(
                type = value
            )
        }
    }

    fun onRecurringChange(value: Boolean) {
        _uiState.update {
            it.copy(
                isRecurring = value
            )
        }
    }

    fun onNotesChange(value: String) {
        _uiState.update {
            it.copy(
                notes = value
            )
        }
    }

    fun hideBudgetCreation() {
        _uiState.update {
            it.copy(
                isBudgetCreationRequired = false
            )
        }
    }


}