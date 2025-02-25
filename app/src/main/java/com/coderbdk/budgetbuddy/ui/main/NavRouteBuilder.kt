package com.coderbdk.budgetbuddy.ui.main

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.coderbdk.budgetbuddy.ui.home.HomeScreen
import com.coderbdk.budgetbuddy.ui.transaction.TransactionScreen

fun NavGraphBuilder.navRouteBuilder(
    navController: NavHostController
) {
    composable<Home> {
        HomeScreen(navController)
    }
    composable<Transaction> {
        TransactionScreen(navController)
    }
}