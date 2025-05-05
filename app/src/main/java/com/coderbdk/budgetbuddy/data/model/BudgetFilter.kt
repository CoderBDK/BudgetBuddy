package com.coderbdk.budgetbuddy.data.model

data class BudgetFilter(
    val query: String? = null,
    val expenseCategoryId: Int? = null,
    val period: BudgetPeriod? = null,
    val startDate: Long? = null,
    val endDate: Long? = null,
)