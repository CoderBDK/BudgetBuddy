package com.coderbdk.budgetbuddy.ui.navigation

import androidx.annotation.StringRes
import androidx.navigation.NavDestination
import com.coderbdk.budgetbuddy.R
import com.coderbdk.budgetbuddy.data.model.TransactionType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

@Serializable
sealed class Screen(
    @StringRes
    @SerialName("title")
    val title: Int
) {

    @Serializable
    data object Home : Screen(R.string.nav_title_home)

    @Serializable
    data object AddTransaction : Screen(R.string.nav_title_add_transaction)

    @Serializable
    data object Budgets : Screen(R.string.nav_title_budgets)

    @Serializable
    data object Analytics : Screen(R.string.nav_title_analytics)

    @Serializable
    data object Settings : Screen(R.string.nav_title_settings)

    @Serializable
    data object Transactions : Screen(R.string.nav_title_transactions)

    @Serializable
    data class TransactionDetails(val transactionData: String) :
        Screen(R.string.nav_title_transaction_details)

    @Serializable
    data class CategoryManage(val type: TransactionType) :
        Screen(R.string.nav_title_category_manage)
}

private val titleMap by lazy {
    Screen::class.sealedSubclasses.associate {
        val title = getTitle(it)
        it.qualifiedName to title
    }
}

private fun getTitle(it: KClass<out Screen>): Int? {
    return when {
        it.primaryConstructor != null -> {
            val constructor = it.primaryConstructor
            val parameters = constructor?.parameters ?: emptyList()
            if (parameters.isNotEmpty()) {
                val argumentMap = parameters.associateWith { param ->
                    when (param.type.classifier) {
                        String::class -> ""
                        Int::class -> 0
                        Boolean::class -> false
                        TransactionType::class -> TransactionType.INCOME
                        else -> null
                    }
                }
                constructor?.callBy(argumentMap)?.title
            } else {
                it.objectInstance?.title
            }
        }

        else -> {
            it.objectInstance?.title
        }
    }
}

fun NavDestination.getNavDestinationTitle(@StringRes defaultTitle: Int): Int {
    val routeKey = route?.substringBefore("/") ?: ""
    return titleMap[routeKey] ?: defaultTitle
}


