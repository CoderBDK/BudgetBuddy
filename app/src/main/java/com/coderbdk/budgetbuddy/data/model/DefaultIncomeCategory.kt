package com.coderbdk.budgetbuddy.data.model


import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import android.content.Context
import com.coderbdk.budgetbuddy.R

enum class DefaultIncomeCategory(@ColorRes val colorResId: Int, val description: String) {
    SALARY(R.color.salary_color, "Regular salary"),
    BUSINESS(R.color.business_color, "Income from business"),
    INVESTMENT(R.color.investment_color, "Profit from investments"),
    FREELANCE(R.color.freelance_color, "Freelancing income"),
    GIFT(R.color.gift_color, "Gift or donation"),
    RENTAL(R.color.rental_color, "Income from rent"),
    OTHERS(R.color.others_income_color, "Other sources");

    fun getColor(context: Context): Int {
        return ContextCompat.getColor(context, colorResId)
    }
}