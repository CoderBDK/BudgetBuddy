package com.coderbdk.budgetbuddy.ui.budget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coderbdk.budgetbuddy.data.db.entity.ExpenseCategory
import com.coderbdk.budgetbuddy.data.model.BudgetPeriod
import com.coderbdk.budgetbuddy.data.model.BudgetWithCategory
import com.coderbdk.budgetbuddy.ui.components.DropDownEntry
import com.coderbdk.budgetbuddy.ui.components.DropDownMenu
import com.coderbdk.budgetbuddy.ui.app.FabAction
import com.coderbdk.budgetbuddy.ui.app.MainViewModel
import com.coderbdk.budgetbuddy.utils.TextUtils.capitalizeFirstLetter


@Composable
fun BudgetScreen(viewModel: BudgetViewModel = hiltViewModel(), mainViewModel: MainViewModel) {

    val budgets by viewModel.budgetsFlow.collectAsStateWithLifecycle(emptyList())
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val fabAction by mainViewModel.fabAction.collectAsState()
    val expenseCategoryList by viewModel.expenseCategories.collectAsState(initial = emptyList())

    var showDialog by remember { mutableStateOf(false) }

    ShowBudgetExistsDialog(
        uiState.budgetExists,
        viewModel::updateBudgetCancel,
        viewModel::updateBudget
    )
    LaunchedEffect(fabAction) {
        if (fabAction is FabAction.AddBudget) {
            showDialog = true
            mainViewModel.clearAction()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Your Budget", fontSize = 22.sp, fontWeight = FontWeight.Bold)

        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
        ) {
            items(budgets) { budget ->
                BudgetItem(budget)
            }
        }

    }

    if (showDialog) {
        AddBudgetDialog(
            expenseCategoryList = expenseCategoryList,
            onDismiss = { showDialog = false },
            onSave = { category, amount, period ->
                viewModel.addBudget(category.id, period, amount)
                showDialog = false
            }
        )
    }
}

@Composable
fun BudgetItem(budgetWithCategory: BudgetWithCategory) {
    val budget = budgetWithCategory.budget
    val spentAmount = budget.spentAmount
    val totalBudget = budget.limitAmount
    val progress = (if (totalBudget > 0) spentAmount / totalBudget else 0f).toFloat()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.elevatedCardElevation()
    ) {
        ListItem(
            overlineContent = {
                Text(
                    "Spent: ${spentAmount.toInt()} / ${budget.limitAmount.toInt()} $",
                )
            },
            headlineContent = {
                Text("${budgetWithCategory.expenseCategory?.name}: ${spentAmount}/${totalBudget}")
            },
            supportingContent = {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = if (progress > 1f) Color.Red.copy(0.6f) else Color.Blue.copy(0.6f)
                )
            }
        )
    }

}


@Composable
fun AddBudgetDialog(
    expenseCategoryList: List<ExpenseCategory>,
    onDismiss: () -> Unit,
    onSave: (ExpenseCategory, Double, BudgetPeriod) -> Unit
) {
    var category by remember { mutableStateOf(expenseCategoryList[0]) }
    var amount by remember { mutableStateOf("") }
    var period by remember { mutableStateOf(BudgetPeriod.DAILY) }
    var selectedPeriodIndex by remember { mutableIntStateOf(BudgetPeriod.valueOf(period.name).ordinal) }
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    val categoryEntries = remember {
        expenseCategoryList.map {
            DropDownEntry(
                title = it.name.lowercase().capitalizeFirstLetter(),
                data = it
            )
        }
    }
    val periodEntries = remember {
        BudgetPeriod.entries.map {
            DropDownEntry(
                title = it.name.lowercase().capitalizeFirstLetter(),
                data = it
            )
        }
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Budget") },
        text = {
            Column {
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    ),
                    singleLine = true,
                    maxLines = 1
                )
                Spacer(Modifier.padding(4.dp))
                DropDownMenu(
                    modifier = Modifier,
                    title = "Choose Budget Category",
                    entries = categoryEntries,
                    selectedIndex = selectedCategoryIndex,
                    onSelected = { data, index ->
                        selectedCategoryIndex = index
                        category = data
                    }
                )
                Spacer(Modifier.padding(4.dp))
                DropDownMenu(
                    modifier = Modifier,
                    title = "Choose Budget Period",
                    entries = periodEntries,
                    selectedIndex = selectedPeriodIndex,
                    onSelected = { data, index ->
                        selectedPeriodIndex = index
                        period = data
                    }
                )

            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val budgetAmount = amount.toDoubleOrNull()
                    onSave(category, budgetAmount ?: 0.0, period)
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}


@Composable
fun ShowBudgetExistsDialog(budgetExists: Boolean, onCancel: () -> Unit, onUpdate: () -> Unit) {
    if (budgetExists) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Budget Exists") },
            text = { Text("A budget for this category and period already exists. Do you want to update it?") },
            confirmButton = {
                Button(onClick = onUpdate) {
                    Text("Update")
                }
            },
            dismissButton = {
                Button(onClick = onCancel) {
                    Text("Cancel")
                }
            }
        )
    }
}
