package com.coderbdk.budgetbuddy.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.coderbdk.budgetbuddy.data.model.BudgetCategory
import com.coderbdk.budgetbuddy.data.model.BudgetPeriod

@Entity(
    tableName = "budgets",
    indices = [Index(value = ["category", "period"], unique = true)]
)
data class Budget(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: BudgetCategory,
    val period: BudgetPeriod,
    @ColumnInfo(name = "limit_amount")
    val limitAmount: Double,
    @ColumnInfo(name = "spent_amount")
    val spentAmount: Double = 0.0,
)
