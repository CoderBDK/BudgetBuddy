package com.coderbdk.budgetbuddy.data.model

import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import android.content.Context
import com.coderbdk.budgetbuddy.R

enum class DefaultExpenseCategory(@ColorRes val colorResId: Int, val description: String) {
    FOOD(R.color.food_color, "Food & Beverages"),
    TRANSPORTATION(R.color.transportation_color, "Transportation Expenses"),
    ENTERTAINMENT(R.color.entertainment_color, "Entertainment & Recreation"),
    HEALTHCARE(R.color.healthcare_color, "Healthcare & Medical"),
    EDUCATION(R.color.education_color, "Education & Learning"),
    RENT(R.color.rent_color, "House Rent"),
    SAVINGS(R.color.savings_color, "Savings"),
    OTHERS(R.color.others_expense_color, "Other Expenses");

    fun getColor(context: Context): Int {
        return ContextCompat.getColor(context, colorResId)
    }
}