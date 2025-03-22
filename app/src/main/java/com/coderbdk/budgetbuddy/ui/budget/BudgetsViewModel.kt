package com.coderbdk.budgetbuddy.ui.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coderbdk.budgetbuddy.data.db.entity.Budget
import com.coderbdk.budgetbuddy.data.model.BudgetCategory
import com.coderbdk.budgetbuddy.data.model.BudgetPeriod
import com.coderbdk.budgetbuddy.domain.budget.usecase.GetBudgetByCategoryPeriodUseCase
import com.coderbdk.budgetbuddy.domain.budget.usecase.GetBudgetsUseCase
import com.coderbdk.budgetbuddy.domain.budget.usecase.InsertBudgetUseCase
import com.coderbdk.budgetbuddy.domain.budget.usecase.UpdateBudgetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class BudgetUiState(
    val budgetExists: Boolean = false,
    val newBudget: Budget? = null
)

@HiltViewModel
class BudgetViewModel @Inject constructor(
    getBudgetsUseCase: GetBudgetsUseCase,
    private val getBudgetByCategoryPeriodUseCase: GetBudgetByCategoryPeriodUseCase,
    private val insertBudgetUseCase: InsertBudgetUseCase,
    private val updateBudgetUseCase: UpdateBudgetUseCase
) : ViewModel() {

    val budgetsFlow = getBudgetsUseCase.invoke()

    private val _uiState = MutableStateFlow(BudgetUiState())
    val uiState: StateFlow<BudgetUiState> = _uiState.asStateFlow()

    fun addBudget(category: BudgetCategory, period: BudgetPeriod, amount: Double) {
        viewModelScope.launch {
            val existingBudget = getBudgetByCategoryPeriodUseCase(category, period)
            if (existingBudget != null) {
                _uiState.update {
                    it.copy(
                        budgetExists = true,
                        newBudget = existingBudget.copy(
                            category = category,
                            period = period,
                            limitAmount = amount
                        )
                    )
                }
            } else {
                insertBudgetUseCase(Budget(0, category, period, amount, 0.0))
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