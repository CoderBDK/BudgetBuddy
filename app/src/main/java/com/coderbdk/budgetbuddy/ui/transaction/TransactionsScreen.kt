package com.coderbdk.budgetbuddy.ui.transaction

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.coderbdk.budgetbuddy.ui.main.Screen
import com.coderbdk.budgetbuddy.ui.transaction.content.TransactionItem
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun TransactionsScreen(
    navController: NavController,
    viewModel: TransactionsViewModel = hiltViewModel()
) {
    val transactions = viewModel.transactions.collectAsLazyPagingItems()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchContent()
        LazyColumn {
            items(transactions.itemCount) {
                val item = transactions[it]
                if (item != null) {
                    TransactionItem(item) {
                        navController.navigate(Screen.TransactionDetails("${Json.encodeToString(item)}"))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchContent() {
    var text by remember { mutableStateOf("") }
    ElevatedCard(
        modifier = Modifier.padding(8.dp)
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    "search"
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        text = ""
                    }
                ) {
                    Icon(
                        Icons.Default.Clear,
                        "search"
                    )
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
                keyboardType = KeyboardType.Text
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

