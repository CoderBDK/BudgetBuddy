package com.coderbdk.budgetbuddy.ui.main

import androidx.navigation.NavDestination
import com.coderbdk.budgetbuddy.data.model.TransactionType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

@Serializable
sealed class Screen(
    @SerialName("title")
    val title: String
) {
    @Serializable
    data object Home : Screen("Home")

    @Serializable
    data object AddTransaction : Screen("Add Transaction")

    @Serializable
    data object Budgets : Screen("Budgets")

    @Serializable
    data object Analytics : Screen("Analytics")

    @Serializable
    data object Settings : Screen("Settings")

    @Serializable
    data object Transactions : Screen("Transactions")

    @Serializable
    data class TransactionDetails(val transactionData: String) : Screen("Transaction Details")

    @Serializable
    data class CategoryManage(val type: TransactionType): Screen("CategoryManage")
}

private val titleMap by lazy {
    Screen::class.sealedSubclasses.associate {
        val title = getTitle(it)
        it.qualifiedName to title
    }
}

private fun getTitle(it: KClass<out Screen>): String? {
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

fun NavDestination.getNavDestinationTitle(defaultTitle: String): String {
    val routeKey = route?.substringBefore("/") ?: ""
    return titleMap[routeKey] ?: defaultTitle
}


