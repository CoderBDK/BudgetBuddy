package com.coderbdk.budgetbuddy.data.model

data class TransactionFilter(
    val query: String? = null,
    val type: TransactionType? = null,
    val expenseCategoryId: Int? = null,
    val incomeCategoryId: Int? = null,
    val period: BudgetPeriod? = null,
    val startDate: Long? = null,
    val endDate: Long? = null,
    val isRecurring: Boolean? = null
)