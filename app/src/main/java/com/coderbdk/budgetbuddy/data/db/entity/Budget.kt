package com.coderbdk.budgetbuddy.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.coderbdk.budgetbuddy.data.model.BudgetPeriod

@Entity(tableName = "budgets")
data class Budget(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val category: String,
    val limitAmount: Double,
    val spentAmount: Double = 0.0,
    val period: BudgetPeriod
)
