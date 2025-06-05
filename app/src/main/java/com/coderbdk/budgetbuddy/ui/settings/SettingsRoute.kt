package com.coderbdk.budgetbuddy.ui.settings

import androidx.compose.runtime.Composable

@Composable
fun SettingsRoute(viewModel: SettingsViewModel) {
    SettingsScreen(
        isEnable = viewModel.isDarkTheme,
        toggleTheme = viewModel::toggleTheme
    )
}
