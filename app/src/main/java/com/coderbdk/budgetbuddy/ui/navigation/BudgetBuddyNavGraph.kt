package com.coderbdk.budgetbuddy.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.coderbdk.budgetbuddy.ui.analytics.AnalyticsRoute
import com.coderbdk.budgetbuddy.ui.budget.BudgetRoute
import com.coderbdk.budgetbuddy.ui.category.CategoryManageRoute
import com.coderbdk.budgetbuddy.ui.home.HomeScreenRoute
import com.coderbdk.budgetbuddy.ui.main.MainViewModel
import com.coderbdk.budgetbuddy.ui.settings.SettingsRoute
import com.coderbdk.budgetbuddy.ui.settings.SettingsViewModel
import com.coderbdk.budgetbuddy.ui.transaction.TransactionsRoute
import com.coderbdk.budgetbuddy.ui.transaction.add.AddTransactionRoute
import com.coderbdk.budgetbuddy.ui.transaction.details.TransactionDetailsRoute
import kotlinx.serialization.json.Json

@Composable
fun BudgetBuddyNavGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    settingsViewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    val navActions = BudgetBuddyNavActions(navController)

    NavHost(
        navController = navController,
        startDestination = Screen.Home,
        modifier = modifier,
        enterTransition = { slideInHorizontally(initialOffsetX = { -it }) + fadeIn() },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() },
    ) {
        composable<Screen.Home> {
            HomeScreenRoute(
                onNavigateToAddTransaction = navActions::navigateToAddTransaction,
                onNavigateToBudgets = navActions::navigateToBudgets,
                onNavigateToTransactions = navActions::navigateToTransactions
            )
        }
        composable<Screen.Budgets> {
            BudgetRoute(mainViewModel = mainViewModel)
        }
        composable<Screen.AddTransaction> {
            AddTransactionRoute(
                onNavigateToBudgets = navActions::navigateToBudgets,
                onNavigateToCategoryManage = navActions::navigateToCategoryManage
            )
        }
        composable<Screen.Analytics> {
            AnalyticsRoute(onNavigateToCategoryManage = navActions::navigateToCategoryManage)
        }
        composable<Screen.Settings> {
            SettingsRoute(settingsViewModel)
        }

        composable<Screen.Transactions> {
            TransactionsRoute(
                onNavigateToTransactionDetails = navActions::navigateToTransactionDetails
            )
        }
        composable<Screen.TransactionDetails> {
            TransactionDetailsRoute(
                Json.decodeFromString(it.toRoute<Screen.TransactionDetails>().transactionData)
            )
        }
        composable<Screen.CategoryManage> {
            CategoryManageRoute(it.toRoute<Screen.CategoryManage>().type)
        }
    }
}


