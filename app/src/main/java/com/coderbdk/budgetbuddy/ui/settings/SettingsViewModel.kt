package com.coderbdk.budgetbuddy.ui.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
) : ViewModel() {
    var isDarkTheme by mutableStateOf(false)
        private set

    init {
      //  isDarkTheme = prefs.isDarkTheme()
    }
    fun toggleTheme() {
        isDarkTheme = !isDarkTheme
       // prefs.updateTheme(isDarkTheme)
    }
}