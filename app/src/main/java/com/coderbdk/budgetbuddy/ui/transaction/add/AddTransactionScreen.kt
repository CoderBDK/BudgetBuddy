package com.coderbdk.budgetbuddy.ui.transaction.add


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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.coderbdk.budgetbuddy.data.db.entity.ExpenseCategory
import com.coderbdk.budgetbuddy.data.db.entity.IncomeCategory
import com.coderbdk.budgetbuddy.data.model.BudgetPeriod
import com.coderbdk.budgetbuddy.data.model.TransactionType
import com.coderbdk.budgetbuddy.ui.components.DropDownEntry
import com.coderbdk.budgetbuddy.ui.components.DropDownMenu
import com.coderbdk.budgetbuddy.ui.theme.BudgetBuddyTheme
import com.coderbdk.budgetbuddy.utils.TextUtils.capitalizeFirstLetter

@Composable
fun AddTransactionScreen(
    expenseCategoryList: List<ExpenseCategory>,
    incomeCategoryList: List<IncomeCategory>,
    uiState: TransactionUiState,
    uiEvent: TransactionUiEvent,
    onNavigateToCategoryManage: (TransactionType) -> Unit
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
    var selectedExpenseCategoryIndex by remember { mutableIntStateOf(0) }
    var selectedIncomeCategoryIndex by remember { mutableIntStateOf(0) }
    val expenseCategoryEntries by remember(expenseCategoryList) {
        derivedStateOf {
            buildList {
                add(DropDownEntry(title = "---", data = null))
                addAll(
                    expenseCategoryList.map { category ->
                        DropDownEntry(
                            title = category.name.lowercase().capitalizeFirstLetter(),
                            data = category
                        )
                    }
                )
            }
        }
    }

    val incomeCategoryEntries by remember(incomeCategoryList) {
        derivedStateOf {
            buildList {
                add(DropDownEntry(title = "---", data = null))
                addAll(
                    incomeCategoryList.map {
                        DropDownEntry(
                            title = it.name.lowercase().capitalizeFirstLetter(),
                            data = it
                        )
                    }
                )
            }
        }

    }

    var selectedPeriodIndex by remember { mutableIntStateOf(0) }
    val periodEntries by remember {
        derivedStateOf {
            buildList {
                add(DropDownEntry(title = "---", data = null))
                addAll(
                    BudgetPeriod.entries.map {
                        DropDownEntry(
                            title = it.name.lowercase().capitalizeFirstLetter(),
                            data = it
                        )
                    }
                )
            }
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
            title = "Transaction Type",
            entries = typeEntries,
            selectedIndex = selectedTypeIndex,
            onSelected = { data, index ->
                selectedTypeIndex = index
                uiEvent.onTypeChange(data)
            }
        )
        if (uiState.type == TransactionType.EXPENSE) {
            Spacer(Modifier.padding(8.dp))

            DropDownMenu(
                modifier = Modifier,
                title = "Transaction Category",
                entries = expenseCategoryEntries,
                selectedIndex = selectedExpenseCategoryIndex,
                trailingContent = {
                    IconButton(
                        onClick = {
                            onNavigateToCategoryManage(TransactionType.EXPENSE)
                        }
                    ) {
                        Icon(Icons.Default.Settings, "manage")
                    }
                },
                onSelected = { data, index ->
                    selectedExpenseCategoryIndex = index
                    uiEvent.onExpenseCategoryChange(data)
                }
            )
            Spacer(Modifier.padding(8.dp))
            DropDownMenu(
                modifier = Modifier,
                title = "Transaction Period",
                entries = periodEntries,
                selectedIndex = selectedPeriodIndex,
                onSelected = { data, index ->
                    selectedPeriodIndex = index
                    uiEvent.onPeriodChange(data)
                }
            )
        } else {
            Spacer(Modifier.padding(8.dp))
            DropDownMenu(
                modifier = Modifier,
                title = "Transaction Category",
                entries = incomeCategoryEntries,
                selectedIndex = selectedIncomeCategoryIndex,
                trailingContent = {
                    IconButton(
                        onClick = {
                            onNavigateToCategoryManage(TransactionType.INCOME)
                        }
                    ) {
                        Icon(Icons.Default.Settings, "manage")
                    }
                },
                onSelected = { data, index ->
                    selectedIncomeCategoryIndex = index
                    uiEvent.onIncomeCategoryChange(data)
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
            expenseCategoryList = emptyList(),
            incomeCategoryList = emptyList(),
            uiState = TransactionUiState(),
            uiEvent = TransactionUiEvent({}, {}, {}, {}, {}, {}, {}, {}),
            onNavigateToCategoryManage = {}
        )
    }
}