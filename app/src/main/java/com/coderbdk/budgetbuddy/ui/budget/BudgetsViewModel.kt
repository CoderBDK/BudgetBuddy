package com.coderbdk.budgetbuddy.ui.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coderbdk.budgetbuddy.data.db.entity.Budget
import com.coderbdk.budgetbuddy.data.db.entity.ExpenseCategory
import com.coderbdk.budgetbuddy.data.model.BudgetPeriod
import com.coderbdk.budgetbuddy.domain.usecase.budget.GetBudgetByCategoryPeriodUseCase
import com.coderbdk.budgetbuddy.domain.usecase.budget.GetBudgetsWithExpenseCategoryUseCase
import com.coderbdk.budgetbuddy.domain.usecase.budget.InsertBudgetUseCase
import com.coderbdk.budgetbuddy.domain.usecase.budget.UpdateBudgetUseCase
import com.coderbdk.budgetbuddy.domain.usecase.transaction.GetAllExpenseCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class BudgetUiState(
    val budgetExists: Boolean = false,
    val newBudget: Budget? = null
)

@HiltViewModel
class BudgetViewModel @Inject constructor(
    getBudgetsWithExpenseCategoryUseCase: GetBudgetsWithExpenseCategoryUseCase,
    private val getBudgetByCategoryPeriodUseCase: GetBudgetByCategoryPeriodUseCase,
    private val insertBudgetUseCase: InsertBudgetUseCase,
    private val updateBudgetUseCase: UpdateBudgetUseCase,
    getAllExpenseCategoriesUseCase: GetAllExpenseCategoriesUseCase
) : ViewModel() {

    val budgetsFlow = getBudgetsWithExpenseCategoryUseCase.invoke()

    private val _uiState = MutableStateFlow(BudgetUiState())
    val uiState: StateFlow<BudgetUiState> = _uiState.asStateFlow()

    val expenseCategories: StateFlow<List<ExpenseCategory>> =
        getAllExpenseCategoriesUseCase().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addBudget(
        categoryId: Int,
        period: BudgetPeriod,
        amount: Double,
        date: Pair<Long, Long>
    ) {
        viewModelScope.launch {
            val existingBudget = getBudgetByCategoryPeriodUseCase(categoryId, period)
            if (existingBudget != null) {
                _uiState.update {
                    it.copy(
                        budgetExists = true,
                        newBudget = existingBudget.copy(
                            expenseCategoryId = categoryId,
                            period = period,
                            limitAmount = amount
                        )
                    )
                }
            } else {
                insertBudgetUseCase(Budget(0, categoryId, period, amount, 0.0, date.first, date.second))
            }
        }
    }

    fun updateBudgetCancel() {
        resetBudgetState()
    }

    private fun resetBudgetState() {
        _uiState.update {
            it.copy(
                budgetExists = false,
                newBudget = null
            )
        }
    }

    fun updateBudget() {
        viewModelScope.launch {
            _uiState.value.newBudget?.let { budget ->
                try {
                    updateBudgetUseCase(budget)
                    resetBudgetState()
                } catch (e: Exception) {
                    resetBudgetState()
                }
            } ?: run {
                resetBudgetState()
            }
        }
    }
}