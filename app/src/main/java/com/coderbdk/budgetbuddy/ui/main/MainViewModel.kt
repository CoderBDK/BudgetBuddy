package com.coderbdk.budgetbuddy.ui.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {
    private val _fabAction = MutableStateFlow<FabAction?>(null)
    val fabAction: StateFlow<FabAction?> = _fabAction.asStateFlow()

    private val _fabVisible = MutableStateFlow(true)
    val fabVisible: StateFlow<Boolean> = _fabVisible.asStateFlow()

    fun performFabAction(action: FabAction) {
        _fabAction.value = action
    }

    fun clearAction() {
        _fabAction.value = null
    }


    fun setFabVisibility(isVisible: Boolean) {
        _fabVisible.value = isVisible
    }
}

sealed class FabAction {
    data object AddTransaction : FabAction()
    data object AddBudget : FabAction()
}
