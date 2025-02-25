package com.coderbdk.budgetbuddy.ui.home

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.coderbdk.budgetbuddy.ui.main.Transaction

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    Button(onClick = {
        navController.navigate(Transaction)
    }) {
        Text("Goto Transaction")
    }
}