package com.coderbdk.budgetbuddy.ui.category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.coderbdk.budgetbuddy.data.db.entity.ExpenseCategory
import com.coderbdk.budgetbuddy.data.db.entity.IncomeCategory
import com.coderbdk.budgetbuddy.data.model.TransactionType
import com.coderbdk.budgetbuddy.ui.components.DropDownEntry
import com.coderbdk.budgetbuddy.ui.components.DropDownMenu
import com.coderbdk.budgetbuddy.ui.main.Screen
import com.coderbdk.budgetbuddy.utils.TextUtils.capitalizeFirstLetter

@Composable
fun CategoryManageScreen(
    navController: NavController,
    type: TransactionType,
    viewModel: CategoryManageViewModel = hiltViewModel(),
) {
    val expenseCategoryList by viewModel.expenseCategories.collectAsState(initial = emptyList())
    val incomeCategoryList by viewModel.incomeCategories.collectAsState(initial = emptyList())

    var selectedTypeIndex by remember { mutableIntStateOf(TransactionType.valueOf(type.name).ordinal) }
    val typeEntries = remember {
        TransactionType.entries.map {
            DropDownEntry(
                title = it.name.lowercase().capitalizeFirstLetter(),
                data = it
            )
        }
    }

    Column(
        Modifier.fillMaxSize().padding(8.dp)
    ) {
        DropDownMenu(
            modifier = Modifier,
            title = "Transaction Type",
            entries = typeEntries,
            selectedIndex = selectedTypeIndex,
            trailingContent = {
                IconButton(
                    onClick = {

                    }
                ) {
                    Icon(Icons.Default.Add,"add")
                }
            },
            onSelected = { _, index ->
                selectedTypeIndex = index
            }
        )

        when (typeEntries[selectedTypeIndex].data) {
            TransactionType.EXPENSE -> {
                ExpenseCategoryList(expenseCategoryList)
            }
            TransactionType.INCOME -> {
                IncomeCategoryList(incomeCategoryList)
            }
        }
    }
}

@Composable
fun ExpenseCategoryList(list: List<ExpenseCategory>) {
    LazyColumn {
        items(list) {
            ListItem(
                leadingContent = {
                    Box (
                        Modifier.size(48.dp).background(
                            Color(it.colorCode?:0xFFCCCCC),
                            CircleShape
                        )
                    ) {

                    }
                },
                headlineContent = {
                    Text(it.name)
                },
                supportingContent = {
                    Text(it.description?:"---")
                },
                trailingContent = {
                    Column {
                        IconButton(
                            onClick = {

                            }
                        ) {
                            Icon(Icons.Default.Delete,"delete", tint = MaterialTheme.colorScheme.error)
                        }
                        IconButton(
                            onClick = {

                            }
                        ) {
                            Icon(Icons.Default.Edit,"edit")
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
                    Box (
                        Modifier.size(48.dp).background(
                            Color(it.colorCode?:0xFFCCCCC),
                            CircleShape
                        )
                    ) {

                    }
                },
                headlineContent = {
                    Text(it.name)
                },
                supportingContent = {
                    Text(it.description?:"---")
                },
                trailingContent = {
                    Column {
                        IconButton(
                            onClick = {

                            }
                        ) {
                            Icon(Icons.Default.Delete,"delete", tint = MaterialTheme.colorScheme.error)
                        }
                        IconButton(
                            onClick = {

                            }
                        ) {
                            Icon(Icons.Default.Edit,"edit")
                        }
                    }
                }
            )
            HorizontalDivider()
        }
    }

}
