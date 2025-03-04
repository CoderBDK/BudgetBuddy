package com.coderbdk.budgetbuddy.ui.transaction


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.coderbdk.budgetbuddy.data.model.BudgetCategory
import com.coderbdk.budgetbuddy.data.model.BudgetPeriod
import com.coderbdk.budgetbuddy.data.model.TransactionType
import com.coderbdk.budgetbuddy.ui.components.DropDownEntry
import com.coderbdk.budgetbuddy.ui.components.DropDownMenu
import com.coderbdk.budgetbuddy.ui.main.Screen
import com.coderbdk.budgetbuddy.ui.theme.BudgetBuddyTheme
import com.coderbdk.budgetbuddy.ui.transaction.dialog.AlertDialogBudgetCreate
import com.coderbdk.budgetbuddy.utils.TextUtils.capitalizeFirstLetter

@Composable
fun AddTransactionScreen(
    navController: NavController,
    viewModel: AddTransactionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isBudgetCreationRequired) {
        AlertDialogBudgetCreate(
            onDismissRequest = viewModel::hideBudgetCreation
        ) {
            navController.navigate(Screen.Budgets)
        }
    }
    AddTransactionScreen(
        uiState = uiState,
        uiEvent = TransactionUiEvent(
            onCategoryChange = viewModel::onCategoryChange,
            onPeriodChange = viewModel::onPeriodChange,
            onRecurringChange = viewModel::onRecurringChange,
            onAmountChange = viewModel::onAmountChange,
            onTypeChange = viewModel::onTypeChange,
            onNotesChange = viewModel::onNotesChange,
            saveTransaction = viewModel::saveTransaction
        )
    )
}

@Composable
fun AddTransactionScreen(
    uiState: TransactionUiState,
    uiEvent: TransactionUiEvent
) {
    var selectedTypeIndex by remember { mutableIntStateOf(TransactionType.valueOf(uiState.type.name).ordinal) }
    val typeEntries = remember {
        TransactionType.entries.map {
            DropDownEntry(
                title = it.name.lowercase().capitalizeFirstLetter(),
                data = it
            )
        }
    }
    var selectedCategoryIndex by remember { mutableIntStateOf(BudgetCategory.valueOf(uiState.category.name).ordinal) }
    val categoryEntries = remember {
        BudgetCategory.entries.map {
            DropDownEntry(
                title = it.name.lowercase().capitalizeFirstLetter(),
                data = it
            )
        }
    }
    var selectedPeriodIndex by remember { mutableIntStateOf(BudgetPeriod.valueOf(uiState.period.name).ordinal) }
    val periodEntries = remember {
        BudgetPeriod.entries.map {
            DropDownEntry(
                title = it.name.lowercase().capitalizeFirstLetter(),
                data = it
            )
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Spacer(Modifier.padding(8.dp))
        OutlinedTextField(
            value = uiState.amount.toString(),
            onValueChange = uiEvent.onAmountChange,
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.padding(8.dp))
        OutlinedTextField(
            value = uiState.notes,
            onValueChange = uiEvent.onNotesChange,
            label = { Text("Notes") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.padding(8.dp))
        DropDownMenu(
            modifier = Modifier,
            title = "Choose Transaction Type",
            entries = typeEntries,
            selectedIndex = selectedTypeIndex,
            onSelected = { data, index ->
                selectedTypeIndex = index
                uiEvent.onTypeChange(data)
            }
        )
        if(uiState.type == TransactionType.EXPENSE) {
            Spacer(Modifier.padding(8.dp))
            DropDownMenu(
                modifier = Modifier,
                title = "Choose Transaction Category",
                entries = categoryEntries,
                selectedIndex = selectedCategoryIndex,
                onSelected = { data, index ->
                    selectedCategoryIndex = index
                    uiEvent.onCategoryChange(data)
                }
            )
            Spacer(Modifier.padding(8.dp))
            DropDownMenu(
                modifier = Modifier,
                title = "Choose Transaction Period",
                entries = periodEntries,
                selectedIndex = selectedPeriodIndex,
                onSelected = { data, index ->
                    selectedPeriodIndex = index
                    uiEvent.onPeriodChange(data)
                }
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = uiState.isRecurring,
                onCheckedChange = uiEvent.onRecurringChange
            )
            Text(text = "Is Recurring")
        }
        Button(
            onClick = uiEvent.saveTransaction,
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isSaving,
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = if (uiState.isSaving) "Saving..." else "Save Transaction")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AddTransactionPreview(modifier: Modifier = Modifier) {
    BudgetBuddyTheme {
        AddTransactionScreen(
            uiState = TransactionUiState(),
            uiEvent = TransactionUiEvent({}, {}, {}, {}, {}, {}, {})
        )
    }
}