package com.coderbdk.budgetbuddy.data.db.entity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "income_categories")
data class IncomeCategory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String? = null,
    @ColumnInfo(name = "color_code")
    val colorCode: Int? = null,
    val isDefault: Boolean = false
)