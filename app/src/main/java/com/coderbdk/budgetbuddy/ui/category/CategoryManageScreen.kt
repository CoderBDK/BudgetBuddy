package com.coderbdk.budgetbuddy.ui.category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.coderbdk.budgetbuddy.data.db.entity.ExpenseCategory
import com.coderbdk.budgetbuddy.data.db.entity.IncomeCategory
import com.coderbdk.budgetbuddy.data.model.TransactionType
import com.coderbdk.budgetbuddy.ui.components.DropDownEntry
import com.coderbdk.budgetbuddy.ui.components.DropDownMenu
import com.coderbdk.budgetbuddy.ui.theme.BudgetBuddyTheme
import com.coderbdk.budgetbuddy.utils.TextUtils.capitalizeFirstLetter

@Composable
fun CategoryManageScreen(
    navController: NavController,
    type: TransactionType,
    viewModel: CategoryManageViewModel = hiltViewModel(),
) {
    val expenseCategoryList by viewModel.expenseCategories.collectAsState(initial = emptyList())
    val incomeCategoryList by viewModel.incomeCategories.collectAsState(initial = emptyList())
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var selectedTypeIndex by remember { mutableIntStateOf(TransactionType.valueOf(type.name).ordinal) }
    val typeEntries = remember {
        TransactionType.entries.map {
            DropDownEntry(
                title = it.name.lowercase().capitalizeFirstLetter(),
                data = it
            )
        }
    }


    if (uiState.showCategoryCreate) {
        DialogCreateCategory(
            onSave = { name, description, colorCode ->
                when (typeEntries[selectedTypeIndex].data) {
                    TransactionType.EXPENSE -> {
                        viewModel.insertExpenseCategory(
                            ExpenseCategory(
                                name = name,
                                description = description,
                                colorCode = colorCode
                            )
                        )
                    }

                    TransactionType.INCOME -> {
                        viewModel.insertIncomeCategory(
                            IncomeCategory(
                                name = name,
                                description = description,
                                colorCode = colorCode
                            )
                        )
                    }
                }
            }
        ) {
           viewModel.hideCreateCategoryDialog()
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        DropDownMenu(
            modifier = Modifier,
            title = "Transaction Type",
            entries = typeEntries,
            selectedIndex = selectedTypeIndex,
            trailingContent = {
                IconButton(
                    onClick = {
                        viewModel.showCreateCategoryDialog()
                    }
                ) {
                    Icon(Icons.Default.Add, "add")
                }
            },
            onSelected = { _, index ->
                selectedTypeIndex = index
            }
        )

        when (typeEntries[selectedTypeIndex].data) {
            TransactionType.EXPENSE -> {
                ExpenseCategoryList(
                    expenseCategoryList,
                    onDelete = viewModel::deleteExpenseCategoryById
                )
            }

            TransactionType.INCOME -> {
                IncomeCategoryList(incomeCategoryList)
            }
        }
    }
}

@Composable
fun ExpenseCategoryList(list: List<ExpenseCategory>, onDelete: (Int) -> Unit) {
    LazyColumn {
        items(list) {
            ListItem(
                leadingContent = {
                    Box(
                        Modifier
                            .size(48.dp)
                            .background(
                                Color(it.colorCode ?: 0xFFCCCCC),
                                CircleShape
                            )
                    ) {

                    }
                },
                headlineContent = {
                    Text(it.name)
                },
                supportingContent = {
                    Text(it.description ?: "---")
                },
                trailingContent = {
                    Column {
                        IconButton(
                            onClick = {
                                onDelete(it.id)
                            }
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                "delete",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                        IconButton(
                            onClick = {

                            }
                        ) {
                            Icon(Icons.Default.Edit, "edit")
                        }
                    }
                }
            )
            HorizontalDivider()
        }
    }

}


@Composable
fun IncomeCategoryList(list: List<IncomeCategory>) {
    LazyColumn {
        items(list) {
            ListItem(
                leadingContent = {
                    Box(
                        Modifier
                            .size(48.dp)
                            .background(
                                Color(it.colorCode ?: 0xFFCCCCC),
                                CircleShape
                            )
                    ) {

                    }
                },
                headlineContent = {
                    Text(it.name)
                },
                supportingContent = {
                    Text(it.description ?: "---")
                },
                trailingContent = {
                    Column {
                        IconButton(
                            onClick = {

                            }
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                "delete",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                        IconButton(
                            onClick = {

                            }
                        ) {
                            Icon(Icons.Default.Edit, "edit")
                        }
                    }
                }
            )
            HorizontalDivider()
        }
    }

}

fun generateColor(): Color? {
    return null
}


@Composable
fun DialogCreateCategory(onSave: (String, String?, Int?) -> Unit, onDismiss: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var color by remember { mutableStateOf<Color?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Category") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    singleLine = true,
                    maxLines = 1
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    maxLines = 1
                )
                Box (
                    Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .height(16.dp)
                        .background(
                            color?:Color.Gray,
                        )
                ){
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                       color = generateColor()
                    },
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text("Generate Category Color")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(name, description, color?.toArgb())
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

@Preview(showBackground = true)
@Composable
fun CategoryManagePreview() {
    BudgetBuddyTheme {
        DialogCreateCategory(onSave = { _, _, _ -> }) { }
    }
}