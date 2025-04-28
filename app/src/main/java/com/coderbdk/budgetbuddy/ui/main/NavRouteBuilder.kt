package com.coderbdk.budgetbuddy.ui.main

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.coderbdk.budgetbuddy.ui.analysis.AnalyticsScreen
import com.coderbdk.budgetbuddy.ui.budget.BudgetScreen
import com.coderbdk.budgetbuddy.ui.category.CategoryManageScreen
import com.coderbdk.budgetbuddy.ui.home.HomeScreen
import com.coderbdk.budgetbuddy.ui.transaction.AddTransactionScreen
import com.coderbdk.budgetbuddy.ui.transaction.TransactionDetailsScreen
import com.coderbdk.budgetbuddy.ui.transaction.TransactionsScreen
import kotlinx.serialization.json.Json

fun NavGraphBuilder.navRouteBuilder(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    composable<Screen.Home> {
        HomeScreen(navController)
    }
    composable<Screen.Budgets> {
        BudgetScreen(mainViewModel = mainViewModel)
    }
    composable<Screen.AddTransaction> {
        AddTransactionScreen(navController)
    }
    composable<Screen.Analytics> {
        AnalyticsScreen(navController)
    }
    composable<Screen.Settings> {
    }

    composable<Screen.Transactions> {
        TransactionsScreen(navController)
    }
    composable<Screen.TransactionDetails> {
        TransactionDetailsScreen(
            navController,
            Json.decodeFromString(it.toRoute<Screen.TransactionDetails>().transactionData)
        )
    }
    composable<Screen.CategoryManage> {
        CategoryManageScreen(navController, it.toRoute<Screen.CategoryManage>().type)
    }
}
