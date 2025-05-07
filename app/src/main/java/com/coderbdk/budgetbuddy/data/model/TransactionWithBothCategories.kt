package com.coderbdk.budgetbuddy.data.model

import androidx.room.Embedded
import com.coderbdk.budgetbuddy.data.db.entity.ExpenseCategory
import com.coderbdk.budgetbuddy.data.db.entity.IncomeCategory
import com.coderbdk.budgetbuddy.data.db.entity.Transaction
import kotlinx.serialization.Serializable

@Serializable
data class TransactionWithBothCategories(
    @Embedded val transaction: Transaction,
    @Embedded(prefix = "expenseCategory_") val expenseCategory: ExpenseCategory?,
    @Embedded(prefix = "incomeCategory_") val incomeCategory: IncomeCategory?
)