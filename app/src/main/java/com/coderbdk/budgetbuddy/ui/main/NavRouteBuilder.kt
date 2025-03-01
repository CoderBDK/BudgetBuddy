package com.coderbdk.budgetbuddy.ui.main

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.coderbdk.budgetbuddy.ui.budget.BudgetScreen
import com.coderbdk.budgetbuddy.ui.home.HomeScreen
import com.coderbdk.budgetbuddy.ui.transaction.AddTransactionScreen
import com.coderbdk.budgetbuddy.ui.transaction.TransactionsScreen

fun NavGraphBuilder.navRouteBuilder(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    composable<Home> {
        HomeScreen(navController)
    }
    composable<Budgets> {
        BudgetScreen(mainViewModel = mainViewModel)
    }
    composable<AddTransaction> {
        AddTransactionScreen(navController)
    }
    composable<Analytics> { }
    composable<Settings> { }

    composable<Transactions> {
        TransactionsScreen()
    }
    composable<TransactionDetails> {
        TransactionsScreen()
    }
}