package com.coderbdk.budgetbuddy.data.repository.impl

import com.coderbdk.budgetbuddy.data.db.dao.BudgetDao
import com.coderbdk.budgetbuddy.data.repository.BudgetRepository
import javax.inject.Inject

class DefaultBudgetRepository @Inject constructor(
    private val budgetDao: BudgetDao
) : BudgetRepository {

}