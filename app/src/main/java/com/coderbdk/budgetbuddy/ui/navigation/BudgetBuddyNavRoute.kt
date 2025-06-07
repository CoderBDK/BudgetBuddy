package com.coderbdk.budgetbuddy.ui.navigation

import androidx.annotation.StringRes
import androidx.navigation.NavDestination
import com.coderbdk.budgetbuddy.R
import com.coderbdk.budgetbuddy.data.model.TransactionType
import kotlinx.serialization.Serializable


@Serializable
sealed class Screen {

    @Serializable
    data object Home : Screen()

    @Serializable
    data object AddTransaction : Screen()

    @Serializable
    data object Budgets : Screen()

    @Serializable
    data object Analytics : Screen()

    @Serializable
    data object Settings : Screen()

    @Serializable
    data object Transactions : Screen()

    @Serializable
    data class TransactionDetails(val transactionData: String) : Screen()

    @Serializable
    data class CategoryManage(val type: TransactionType) : Screen()
}

data class RouteMetadata(
    @StringRes val title: Int,
)

private val routeMetadataMap: Map<String, RouteMetadata> = buildMap {
    put<Screen.Home>(R.string.nav_title_home)
    put<Screen.AddTransaction>(R.string.nav_title_add_transaction)
    put<Screen.Budgets>(R.string.nav_title_budgets)
    put<Screen.Analytics>(R.string.nav_title_analytics)
    put<Screen.Settings>(R.string.nav_title_settings)
    put<Screen.Transactions>(R.string.nav_title_transactions)
    put<Screen.TransactionDetails>(R.string.nav_title_transaction_details)
    put<Screen.CategoryManage>(R.string.nav_title_category_manage)
}

private inline fun <reified T : Screen> MutableMap<String, RouteMetadata>.put(@StringRes title: Int) {
    val key = T::class.qualifiedName ?: error("No qualifiedName")
    put(key, RouteMetadata(title))
}

fun NavDestination.getNavDestinationMetadata(): RouteMetadata? {
    val routeKey = route?.substringBefore("/")?: return null
    return routeMetadataMap[routeKey]
}

fun RouteMetadata?.getNavDestinationTitle(@StringRes defaultTitle: Int): Int {
    return this?.title ?: defaultTitle
}


