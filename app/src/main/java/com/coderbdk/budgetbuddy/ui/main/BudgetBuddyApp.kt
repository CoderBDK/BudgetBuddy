package com.coderbdk.budgetbuddy.ui.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.coderbdk.budgetbuddy.ui.theme.BudgetBuddyTheme

@Composable
fun BudgetBuddyApp() {
    val navController = rememberNavController()
    BudgetBuddyTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Home,
                modifier = Modifier.padding(innerPadding),
                builder = {
                    navRouteBuilder(navController)
                }
            )
        }
    }
}