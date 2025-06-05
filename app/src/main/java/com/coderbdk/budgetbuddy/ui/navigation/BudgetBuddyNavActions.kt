package com.coderbdk.budgetbuddy.ui.navigation

import androidx.navigation.NavController
import com.coderbdk.budgetbuddy.data.model.TransactionType
import com.coderbdk.budgetbuddy.data.model.TransactionWithBothCategories
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class BudgetBuddyNavActions(private val navController: NavController) {
    fun navigateToHome() {
        navController.navigate(Screen.Home) {
            popUpTo<Screen.Home> {
                inclusive = true
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToBudgets() {
        navController.navigate(Screen.Budgets)
    }
    fun navigateToTransactions() {
        navController.navigate(Screen.Transactions)
    }
    fun navigateToAddTransaction() {
        navController.navigate(Screen.AddTransaction)
    }
    fun navigateToCategoryManage(type: TransactionType) {
        navController.navigate(Screen.CategoryManage(type))
    }
    fun navigateToTransactionDetails(transaction: TransactionWithBothCategories) {
        navController.navigate(Screen.TransactionDetails(Json.encodeToString(transaction)))
    }
}