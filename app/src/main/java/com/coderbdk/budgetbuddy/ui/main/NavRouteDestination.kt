package com.coderbdk.budgetbuddy.ui.main

import com.coderbdk.budgetbuddy.data.db.entity.Transaction
import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
object AddTransaction

@Serializable
object Budgets

@Serializable
object Analytics

@Serializable
object Settings

@Serializable
object Transactions

@Serializable
data class TransactionDetails(val transaction: Transaction)