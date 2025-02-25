package com.coderbdk.budgetbuddy.ui.transaction

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController


@Composable
fun TransactionScreen(navController: NavController) {
    Button(onClick = {
        navController.navigateUp()
    }) {
        Text("Back")
    }
}