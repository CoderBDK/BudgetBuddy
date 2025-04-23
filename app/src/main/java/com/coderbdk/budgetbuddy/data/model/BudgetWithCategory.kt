package com.coderbdk.budgetbuddy.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.coderbdk.budgetbuddy.data.db.entity.Budget
import com.coderbdk.budgetbuddy.data.db.entity.ExpenseCategory

data class BudgetWithCategory(
    @Embedded val budget: Budget,
    @Relation(
        parentColumn = "expense_category_id",
        entityColumn = "id"
    )
    val expenseCategory: ExpenseCategory? = null
)