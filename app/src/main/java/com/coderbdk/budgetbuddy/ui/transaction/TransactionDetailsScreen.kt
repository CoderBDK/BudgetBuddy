package com.coderbdk.budgetbuddy.ui.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.coderbdk.budgetbuddy.data.db.entity.Transaction
import com.coderbdk.budgetbuddy.data.model.BudgetCategory
import com.coderbdk.budgetbuddy.data.model.BudgetPeriod
import com.coderbdk.budgetbuddy.data.model.TransactionType
import com.coderbdk.budgetbuddy.ui.components.DropDownEntry
import com.coderbdk.budgetbuddy.ui.components.DropDownMenu
import com.coderbdk.budgetbuddy.ui.home.dateFormatter
import com.coderbdk.budgetbuddy.ui.theme.BudgetBuddyTheme
import com.coderbdk.budgetbuddy.utils.CategoryColorUtils
import com.coderbdk.budgetbuddy.utils.TextUtils.capitalizeFirstLetter
import java.util.Date

@Composable
fun TransactionDetailsScreen(
    navController: NavController,
    transaction: Transaction,
    viewModel: TransactionDetailsViewModel = hiltViewModel()
) {
    Column(
        Modifier.fillMaxSize()
    ) {
        TransactionDetailsContent(
            transaction
        )
    }
}

@Composable
fun TransactionDetailsContent(transaction: Transaction) {
    ElevatedCard(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        if (transaction.type == TransactionType.INCOME) Color(0xFF4CAF50) else CategoryColorUtils.categoryColors[transaction.category?.name]
                            ?: Color.LightGray,
                        CircleShape
                    )
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                if (transaction.category == null) {
                    Text(transaction.type.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                } else {
                    Text(
                        transaction.category.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }


                Text(dateFormatter.format(Date(transaction.date)), fontSize = 12.sp)
            }
            Text(
                text = if (transaction.type == TransactionType.INCOME) "+$${transaction.amount}" else "-$${transaction.amount}",
                color = if (transaction.type == TransactionType.INCOME) Color(0xFF4CAF50) else Color(
                    0xFFF44336
                ),
                fontWeight = FontWeight.Bold
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            TransactionDetailsUpdateContent(
                uiState = TransactionUiState(
                    type = transaction.type,
                    category = transaction.category ?: BudgetCategory.FOOD,
                    period = transaction.period ?: BudgetPeriod.DAILY
                ),
                uiEvent = TransactionUiEvent({}, {}, {}, {}, {}, {}, {})
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ElevatedButton(onClick = {}) {
                Text("Delete")
            }
            ElevatedButton(onClick = {}) {
                Text("Update")
            }
        }
    }
}


@Composable
fun TransactionDetailsUpdateContent(
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
    if (uiState.type == TransactionType.EXPENSE) {
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
}

@Preview(showBackground = true)
@Composable
fun TransactionDetailsPreview() {
    BudgetBuddyTheme {
        Column(Modifier.padding(8.dp)) {
            TransactionDetailsContent(
                Transaction(
                    id = 0,
                    type = TransactionType.INCOME,
                    amount = 0.0,
                    category = BudgetCategory.FOOD,
                    period = BudgetPeriod.DAILY,
                    date = System.currentTimeMillis()
                )

            )
        }
    }
}