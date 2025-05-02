package com.coderbdk.budgetbuddy.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coderbdk.budgetbuddy.data.db.entity.ExpenseCategory
import com.coderbdk.budgetbuddy.data.db.entity.IncomeCategory
import com.coderbdk.budgetbuddy.domain.usecase.category.DeleteExpenseCategoryByIdUseCase
import com.coderbdk.budgetbuddy.domain.usecase.category.DeleteIncomeCategoryByIdUseCase
import com.coderbdk.budgetbuddy.domain.usecase.category.InsertExpenseCategoryUseCase
import com.coderbdk.budgetbuddy.domain.usecase.category.InsertIncomeCategoryUseCase
import com.coderbdk.budgetbuddy.domain.usecase.transaction.GetAllExpenseCategoriesUseCase
import com.coderbdk.budgetbuddy.domain.usecase.transaction.GetAllIncomeCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class CategoryManageUiState(
    val showCategoryCreate: Boolean,
)
@HiltViewModel
class CategoryManageViewModel @Inject constructor(
    getAllExpenseCategoriesUseCase: GetAllExpenseCategoriesUseCase,
    getAllIncomeCategoriesUseCase: GetAllIncomeCategoriesUseCase,
    private val deleteExpenseCategoryByIdUseCase: DeleteExpenseCategoryByIdUseCase,
    private val deleteIncomeCategoryByIdUseCase: DeleteIncomeCategoryByIdUseCase,
    private val insertExpenseCategoryUseCase: InsertExpenseCategoryUseCase,
    private val insertIncomeCategoryUseCase: InsertIncomeCategoryUseCase
) : ViewModel() {

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

    private val _uiState = MutableStateFlow(CategoryManageUiState(
        showCategoryCreate = false
    ))
    val uiState: StateFlow<CategoryManageUiState> = _uiState.asStateFlow()

    fun deleteExpenseCategoryById(categoryId: Int) {
        viewModelScope.launch {
            deleteExpenseCategoryByIdUseCase(categoryId)
        }
    }
    fun deleteIncomeCategoryById(categoryId: Int) {
        viewModelScope.launch {
            deleteIncomeCategoryByIdUseCase(categoryId)
        }
    }
    fun insertExpenseCategory(category: ExpenseCategory) {
        viewModelScope.launch {
            insertExpenseCategoryUseCase(category)
            _uiState.update {
                it.copy(
                    showCategoryCreate = false
                )
            }
        }
    }
    fun insertIncomeCategory(category: IncomeCategory) {
        viewModelScope.launch {
            insertIncomeCategoryUseCase(category)
            _uiState.update {
                it.copy(
                    showCategoryCreate = false
                )
            }
        }
    }

    fun showCreateCategoryDialog() {
        _uiState.update {
            it.copy(
                showCategoryCreate = true
            )
        }
    }

    fun hideCreateCategoryDialog() {
        _uiState.update {
            it.copy(
                showCategoryCreate = false
            )
        }
    }
}