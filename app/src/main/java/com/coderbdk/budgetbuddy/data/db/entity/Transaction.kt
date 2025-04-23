package com.coderbdk.budgetbuddy.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import com.coderbdk.budgetbuddy.data.model.BudgetPeriod
import com.coderbdk.budgetbuddy.data.model.TransactionType
import kotlinx.serialization.Serializable


@Serializable
@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = IncomeCategory::class,
            parentColumns = ["id"],
            childColumns = ["income_category_id"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = ExpenseCategory::class,
            parentColumns = ["id"],
            childColumns = ["expense_category_id"],
            onDelete = ForeignKey.SET_NULL
        ),
    ]
)
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val type: TransactionType,
    val amount: Double,
    @ColumnInfo(name = "income_category_id")
    val incomeCategoryId: Int? = null,
    @ColumnInfo(name = "expense_category_id")
    val expenseCategoryId: Int? = null,
    val period: BudgetPeriod? = null,
    @ColumnInfo(name = "transaction_date")
    val transactionDate: Long,
    val notes: String? = null,
    @ColumnInfo("is_recurring")
    val isRecurring: Boolean = false
)
