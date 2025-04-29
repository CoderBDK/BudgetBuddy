package com.coderbdk.budgetbuddy.ui.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coderbdk.budgetbuddy.data.db.entity.ExpenseCategory
import com.coderbdk.budgetbuddy.data.db.entity.IncomeCategory
import com.coderbdk.budgetbuddy.data.db.entity.Transaction
import com.coderbdk.budgetbuddy.data.model.BudgetPeriod
import com.coderbdk.budgetbuddy.data.model.TransactionType
import com.coderbdk.budgetbuddy.domain.usecase.budget.DoesBudgetExistUseCase
import com.coderbdk.budgetbuddy.domain.usecase.transaction.GetAllExpenseCategoriesUseCase
import com.coderbdk.budgetbuddy.domain.usecase.transaction.GetAllIncomeCategoriesUseCase
import com.coderbdk.budgetbuddy.domain.usecase.transaction.InsertTransactionWithBudgetIncrementUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class TransactionUiState(
    val type: TransactionType = TransactionType.EXPENSE,
    val expenseCategory: ExpenseCategory? = null,
    val incomeCategory: IncomeCategory? = null,
    val period: BudgetPeriod? = null,
    val notes: String = "",
    val amount: Double = 0.0,
    val isRecurring: Boolean = false,
    val isSaving: Boolean = false,
    val isBudgetCreationRequired: Boolean = false,
)

data class TransactionUiEvent(
    val onAmountChange: (String) -> Unit,
    val onExpenseCategoryChange: (ExpenseCategory?) -> Unit,
    val onIncomeCategoryChange: (IncomeCategory?) -> Unit,
    val onPeriodChange: (BudgetPeriod?) -> Unit,
    val onTypeChange: (TransactionType) -> Unit,
    val onRecurringChange: (Boolean) -> Unit,
    val onNotesChange: (String) -> Unit,
    val saveTransaction: () -> Unit
)

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val insertTransactionWithBudgetIncrementUseCase: InsertTransactionWithBudgetIncrementUseCase,
    private val doesBudgetExistUseCase: DoesBudgetExistUseCase,
    getAllExpenseCategoriesUseCase: GetAllExpenseCategoriesUseCase,
    getAllIncomeCategoriesUseCase: GetAllIncomeCategoriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionUiState())
    val uiState: StateFlow<TransactionUiState> = _uiState.asStateFlow()

    val expenseCategories: StateFlow<List<ExpenseCategory>> = getAllExpenseCategoriesUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    val incomeCategories: StateFlow<List<IncomeCategory>> = getAllIncomeCategoriesUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun saveTransaction() {

        viewModelScope.launch {

            val transaction = _uiState.value.run {
                Transaction(
                    type = type,
                    amount = amount,
                    expenseCategoryId = expenseCategory?.id,
                    incomeCategoryId = incomeCategory?.id,
                    period = period,
                    transactionDate = System.currentTimeMillis(),
                    notes = notes,
                    isRecurring = isRecurring
                )
            }

          /*  if (transaction.type == TransactionType.EXPENSE) {
                if (!doesBudgetExistUseCase(
                        transaction.expenseCategoryId!!,
                        transaction.period!!
                    )
                ) {
                    _uiState.update { it.copy(isBudgetCreationRequired = true) }
                    return@launch
                }
            }*/

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


    fun onExpenseCategoryChange(category: ExpenseCategory?) {
        _uiState.update {
            it.copy(
                expenseCategory = category,
                incomeCategory = null
            )
        }
    }
    fun onIncomeCategoryChange(category: IncomeCategory?) {
        _uiState.update {
            it.copy(
                incomeCategory = category,
                expenseCategory = null,
                period = null
            )
        }
    }
    fun onPeriodChange(value: BudgetPeriod?) {
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