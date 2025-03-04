package com.coderbdk.budgetbuddy.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import com.coderbdk.budgetbuddy.data.model.BudgetCategory
import com.coderbdk.budgetbuddy.data.model.BudgetPeriod
import com.coderbdk.budgetbuddy.data.model.TransactionType
import kotlinx.serialization.Serializable


@Serializable
@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = Budget::class,
            parentColumns = ["category", "period"],
            childColumns = ["category", "period"],
            onDelete = CASCADE
        )
    ]
)
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: TransactionType,
    val amount: Double,
    val category: BudgetCategory? = null,
    val period: BudgetPeriod? = null,
    val date: Long,
    val notes: String? = null,
    @ColumnInfo("is_recurring")
    val isRecurring: Boolean = false
)
