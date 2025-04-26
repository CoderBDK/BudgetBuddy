package com.coderbdk.budgetbuddy.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coderbdk.budgetbuddy.data.db.entity.ExpenseCategory
import com.coderbdk.budgetbuddy.data.db.entity.IncomeCategory
import com.coderbdk.budgetbuddy.domain.usecase.category.DeleteExpenseCategoryByIdUseCase
import com.coderbdk.budgetbuddy.domain.usecase.transaction.GetAllExpenseCategoriesUseCase
import com.coderbdk.budgetbuddy.domain.usecase.transaction.GetAllIncomeCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryManageViewModel @Inject constructor(
    getAllExpenseCategoriesUseCase: GetAllExpenseCategoriesUseCase,
    getAllIncomeCategoriesUseCase: GetAllIncomeCategoriesUseCase,
   private val deleteExpenseCategoryByIdUseCase: DeleteExpenseCategoryByIdUseCase
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


    fun deleteExpenseCategoryById(categoryId: Int) {
        viewModelScope.launch {
            deleteExpenseCategoryByIdUseCase(categoryId)
        }
    }

}