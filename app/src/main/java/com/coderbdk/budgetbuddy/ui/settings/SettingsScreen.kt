package com.coderbdk.budgetbuddy.ui.settings


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.coderbdk.budgetbuddy.ui.theme.Typography

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ThemePrefs(
            viewModel.isDarkTheme,
            viewModel::toggleTheme
        )
    }
}

@Composable
fun ThemePrefs(
    isEnable: Boolean,
    toggleTheme: () -> Unit,
) {
    SettingContent(
        title = "App Theme",
    ) {
        ListItem(
            leadingContent = {
                Icon(Icons.Default.BrightnessAuto, contentDescription = "Theme")
            },
            headlineContent = {
                Text("Switch Theme")
            },
            supportingContent = {
                Text("Toggle between day and night.")
            },
            trailingContent = {
                Switch(checked = isEnable, onCheckedChange = { toggleTheme() })
            }
        )
    }

}

@Composable
fun SettingContent(title: String, content: @Composable () -> Unit) {
    Text(title, modifier = Modifier.padding(8.dp), style = Typography.titleMedium)
    Column(
        Modifier.fillMaxWidth()
    ) {
        content()
    }
    HorizontalDivider()
}
