package com.coderbdk.budgetbuddy.ui.transaction

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.coderbdk.budgetbuddy.data.db.entity.ExpenseCategory
import com.coderbdk.budgetbuddy.data.db.entity.IncomeCategory
import com.coderbdk.budgetbuddy.data.model.TransactionFilter
import com.coderbdk.budgetbuddy.data.model.TransactionType
import com.coderbdk.budgetbuddy.ui.main.Screen
import com.coderbdk.budgetbuddy.ui.transaction.content.TransactionItem
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun TransactionsScreen(
    navController: NavController,
    viewModel: TransactionsViewModel = hiltViewModel()
) {
    val transactions = viewModel.filteredTransactions.collectAsLazyPagingItems()
    val filter by viewModel.filter.collectAsState()
    val expenseCategoryList by viewModel.expenseCategories.collectAsState(initial = emptyList())
    val incomeCategoryList by viewModel.incomeCategories.collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize()) {
        SearchAndFilterBar(
            expenseCategoryList = expenseCategoryList,
            incomeCategoryList = incomeCategoryList,
            filter = filter,
            onSearchQueryChange = { viewModel.setSearchQuery(it) },
            onFilterChange = { viewModel.updateFilter(it) }
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(transactions.itemCount) {
                val item = transactions[it]
                if (item != null) {
                    TransactionItem(item) {
                        navController.navigate(Screen.TransactionDetails(Json.encodeToString(item)))
                    }
                }
            }
            transactions.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        item {
                            Box(
                                Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    loadState.append is LoadState.Loading -> {
                        item {
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    loadState.refresh is LoadState.Error -> {
                        val error = (loadState.refresh as LoadState.Error).error
                        item {
                            ErrorMessage(error.message ?: "Something went wrong") {
                                transactions.retry()
                            }
                        }
                    }

                    loadState.append is LoadState.Error -> {
                        val error = (loadState.append as LoadState.Error).error
                        item {
                            ErrorMessage(error.message ?: "Failed to load more") {
                                transactions.retry()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorMessage(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message, color = Color.Red, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}


@Composable
fun SearchAndFilterBar(
    expenseCategoryList: List<ExpenseCategory>,
    incomeCategoryList: List<IncomeCategory>,
    filter: TransactionFilter,
    onSearchQueryChange: (String) -> Unit,
    onFilterChange: (TransactionFilter) -> Unit
) {
    Column {

        SearchContent(
            filter.query ?: "",
            onSearchQueryChange
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterDropdown(
                label = "Type",
                options = TransactionType.entries,
                selectedOption = filter.type,
                onOptionSelected = { onFilterChange(filter.copy(type = it)) }
            )

            if(filter.type == TransactionType.EXPENSE) {
                FilterDropdown(
                    label = "Category",
                    options = expenseCategoryList,
                    selectedOption = expenseCategoryList.find { it.id == filter.expenseCategoryId },
                    onOptionSelected = { onFilterChange(filter.copy(expenseCategoryId = it?.id, incomeCategoryId = null)) },
                    selectedContent = {
                        if (it != null) {
                            Text(text = it.name)
                        } else {
                            Text("Category")
                        }
                    },
                    itemContent = {
                        if (it != null) {
                            Text(text = it.name)
                        }
                    }
                )

            }else {
                FilterDropdown(
                    label = "Category",
                    options = incomeCategoryList,
                    selectedOption = incomeCategoryList.find { it.id == filter.incomeCategoryId },
                    onOptionSelected = { onFilterChange(filter.copy(incomeCategoryId = it?.id, expenseCategoryId = null)) },
                    selectedContent = {
                        if (it != null) {
                            Text(text = it.name)
                        } else {
                            Text("Category")
                        }
                    },
                    itemContent = {
                        if (it != null) {
                            Text(text = it.name)
                        }
                    }
                )

            }

            FilterDropdown(
                label = "Recurring",
                options = listOf(true, false),
                selectedOption = filter.isRecurring,
                onOptionSelected = { onFilterChange(filter.copy(isRecurring = it)) }
            )
        }
    }
}

@Composable
fun <T> FilterDropdown(
    label: String,
    options: List<T>,
    selectedOption: T?,
    onOptionSelected: (T?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(selectedOption?.toString() ?: label) }

    Box(modifier = Modifier.wrapContentSize()) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.padding(4.dp)
        ) {
            Text(selectedText)
            Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(text = { Text(label) }, onClick = {
                onOptionSelected(null)
                selectedText = label
                expanded = false
            })
            options.forEach { option ->
                DropdownMenuItem(text = { Text(option.toString()) }, onClick = {
                    onOptionSelected(option)
                    selectedText = option.toString()
                    expanded = false
                })
            }
        }
    }
}

@Composable
fun <T> FilterDropdown(
    label: String,
    options: List<T>,
    selectedOption: T?,
    onOptionSelected: (T?) -> Unit,
    selectedContent: @Composable (T?) -> Unit,
    itemContent: @Composable (T?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.wrapContentSize()) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.padding(4.dp)
        ) {
            selectedContent(selectedOption)
            Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(text = { Text(label) }, onClick = {
                onOptionSelected(null)
                expanded = false
            })
            options.forEach { option ->
                DropdownMenuItem(text = { itemContent(option)}, onClick = {
                    onOptionSelected(option)
                    expanded = false
                })
            }
        }
    }
}


@Composable
fun SearchContent(
    text: String,
    onTextChange: (String) -> Unit
) {
    ElevatedCard(
        modifier = Modifier.padding(8.dp)
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            leadingIcon = { Icon(Icons.Default.Search, "search") },
            trailingIcon = {
                IconButton(onClick = { onTextChange("") }) {
                    Icon(Icons.Default.Clear, "clear")
                }
            },
            placeholder = { Text("Amount") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
                keyboardType = KeyboardType.Decimal
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchContentPreview() {
    SearchContent("") { }
}

