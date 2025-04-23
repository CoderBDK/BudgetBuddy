package com.coderbdk.budgetbuddy.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.coderbdk.budgetbuddy.data.model.BudgetPeriod


@Entity(
    tableName = "budgets",
    indices = [Index(value = ["expense_category_id", "period"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = ExpenseCategory::class,
            parentColumns = ["id"],
            childColumns = ["expense_category_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Budget(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "expense_category_id")
    val expenseCategoryId: Int,
    val period: BudgetPeriod,
    @ColumnInfo(name = "limit_amount")
    val limitAmount: Double,
    @ColumnInfo(name = "spent_amount")
    val spentAmount: Double = 0.0,
    @ColumnInfo(name = "start_date")
    val startDate: Long,
    @ColumnInfo(name = "end_date")
    val endDate: Long,
    @ColumnInfo(name = "creation_timestamp")
    val creationTimestamp: Long = System.currentTimeMillis()
)
