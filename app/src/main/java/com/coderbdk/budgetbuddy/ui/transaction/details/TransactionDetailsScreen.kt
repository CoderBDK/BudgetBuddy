package com.coderbdk.budgetbuddy.ui.transaction.details


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coderbdk.budgetbuddy.data.db.entity.ExpenseCategory
import com.coderbdk.budgetbuddy.data.db.entity.IncomeCategory
import com.coderbdk.budgetbuddy.data.db.entity.Transaction
import com.coderbdk.budgetbuddy.data.model.BudgetPeriod
import com.coderbdk.budgetbuddy.data.model.TransactionType
import com.coderbdk.budgetbuddy.ui.components.DropDownEntry
import com.coderbdk.budgetbuddy.ui.home.dateFormatter
import com.coderbdk.budgetbuddy.ui.theme.BudgetBuddyTheme
import com.coderbdk.budgetbuddy.ui.transaction.add.TransactionUiState
import com.coderbdk.budgetbuddy.utils.TextUtils.capitalizeFirstLetter
import java.util.Date

@Composable
fun TransactionDetailsScreen(
    expenseCategoryList: List<ExpenseCategory>,
    incomeCategoryList: List<IncomeCategory>,
    transaction: Transaction
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        ElevatedCard(
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            if (transaction.type == TransactionType.INCOME) Color(0xFF4CAF50) else Color(
                                0xFFF44336
                            ),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        if (transaction.type == TransactionType.INCOME) "I" else "E",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = transaction.expenseCategoryId?.toString() ?: transaction.type.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = dateFormatter.format(Date(transaction.transactionDate)),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                Text(
                    text = if (transaction.type == TransactionType.INCOME) "+$${transaction.amount}" else "-$${transaction.amount}",
                    color = if (transaction.type == TransactionType.INCOME) Color(0xFF4CAF50) else Color(
                        0xFFF44336
                    ),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ElevatedCard(
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (expenseCategoryList.isNotEmpty() && incomeCategoryList.isNotEmpty()) {
                    TransactionDetailsUpdateContent(
                        expenseCategoryList,
                        incomeCategoryList,
                        uiState = TransactionUiState(
                            type = transaction.type,
                            period = transaction.period ?: BudgetPeriod.DAILY
                        )
                    )
                } else {
                    Text("No categories available", color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun TransactionDetailsUpdateContent(
    expenseCategoryList: List<ExpenseCategory>,
    incomeCategoryList: List<IncomeCategory>,
    uiState: TransactionUiState,
) {
    var selectedTypeIndex by remember { mutableIntStateOf(TransactionType.valueOf(uiState.type.name).ordinal) }
    val typeEntries = remember {
        TransactionType.entries.map {
            DropDownEntry(
                it.name.lowercase().capitalizeFirstLetter(),
                it
            )
        }
    }

    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    val expenseCategoryEntries = remember {
        expenseCategoryList.map { DropDownEntry(it.name.lowercase().capitalizeFirstLetter(), it) }
    }
    val incomeCategoryEntries = remember {
        incomeCategoryList.map { DropDownEntry(it.name.lowercase().capitalizeFirstLetter(), it) }
    }

    var selectedPeriodIndex by remember { mutableIntStateOf(0) }
    val periodEntries = remember {
        BudgetPeriod.entries.map { DropDownEntry(it.name.lowercase().capitalizeFirstLetter(), it) }
    }

    InfoRow(label = "Transaction Type", value = typeEntries[selectedTypeIndex].title)

    Spacer(modifier = Modifier.height(12.dp))

    if (uiState.type == TransactionType.EXPENSE) {
        InfoRow(label = "Category", value = expenseCategoryEntries[selectedCategoryIndex].title)
        Spacer(modifier = Modifier.height(12.dp))
        InfoRow(label = "Period", value = periodEntries[selectedPeriodIndex].title)
    } else {
        InfoRow(label = "Category", value = incomeCategoryEntries[selectedCategoryIndex].title)
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}


@Preview(showBackground = true)
@Composable
fun TransactionDetailsPreview() {
    BudgetBuddyTheme {
        Column(Modifier.padding(8.dp)) {
            TransactionDetailsScreen(
                emptyList(),
                emptyList(),
                Transaction(
                    id = 0,
                    type = TransactionType.INCOME,
                    amount = 0.0,
                    period = BudgetPeriod.DAILY,
                    transactionDate = System.currentTimeMillis()
                )

            )
        }
    }
}
