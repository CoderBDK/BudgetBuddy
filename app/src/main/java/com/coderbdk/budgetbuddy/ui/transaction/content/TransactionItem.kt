package com.coderbdk.budgetbuddy.ui.transaction.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coderbdk.budgetbuddy.data.db.entity.ExpenseCategory
import com.coderbdk.budgetbuddy.data.db.entity.IncomeCategory
import com.coderbdk.budgetbuddy.data.db.entity.Transaction
import com.coderbdk.budgetbuddy.data.model.BudgetPeriod
import com.coderbdk.budgetbuddy.data.model.TransactionType
import com.coderbdk.budgetbuddy.data.model.TransactionWithBothCategories
import com.coderbdk.budgetbuddy.ui.home.dateFormatter
import com.coderbdk.budgetbuddy.ui.theme.BudgetBuddyTheme
import com.coderbdk.budgetbuddy.utils.TextUtils.capitalizeFirstLetter
import java.util.Date

@Composable
fun TransactionItem(transaction: TransactionWithBothCategories, gotoDetails: () -> Unit) {

    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val colorCode: Int? =
            if (transaction.transaction.type == TransactionType.INCOME) transaction.incomeCategory?.colorCode else transaction.expenseCategory?.colorCode
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    Color(color = colorCode ?: 0xFFFFFFF).copy(alpha = 0.2f),
                    RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (transaction.transaction.type == TransactionType.INCOME) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                contentDescription = null,
                tint = colorCode?.let { Color(it) } ?: Color.Black
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            if (transaction.transaction.type == TransactionType.EXPENSE) {
                Text(
                    transaction.expenseCategory?.name?.lowercase()?.capitalizeFirstLetter()
                        ?: "Unknown",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            } else {
                Text(
                    transaction.incomeCategory?.name?.lowercase()?.capitalizeFirstLetter()
                        ?: "Unknown",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }


            Text(
                dateFormatter.format(Date(transaction.transaction.transactionDate)),
                fontSize = 12.sp
            )
        }
        Text(
            text = if (transaction.transaction.type == TransactionType.INCOME) "+$${transaction.transaction.amount}" else "-$${transaction.transaction.amount}",
            color = if (transaction.transaction.type == TransactionType.INCOME) Color(0xFF4CAF50) else Color(
                0xFFF44336
            ),
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionItemPreview() {
    BudgetBuddyTheme {
        Column(Modifier.padding(8.dp)) {
            TransactionItem(
                transaction = TransactionWithBothCategories(
                    Transaction(
                        id = 0,
                        type = TransactionType.INCOME,
                        amount = 0.0,
                        period = BudgetPeriod.DAILY,
                        transactionDate = System.currentTimeMillis()
                    ),
                    incomeCategory = IncomeCategory(
                        name = "Rental",
                        colorCode = Color(0xFF4CAF50).toArgb()
                    ),
                    expenseCategory = null
                ),
                gotoDetails = {}
            )
            TransactionItem(
                transaction = TransactionWithBothCategories(
                    Transaction(
                        id = 0,
                        type = TransactionType.EXPENSE,
                        amount = 0.0,
                        period = BudgetPeriod.DAILY,
                        transactionDate = System.currentTimeMillis()
                    ),
                    incomeCategory = null,
                    expenseCategory = ExpenseCategory(
                        name = "Food",
                        colorCode = Color(0xFFFF5722).toArgb()
                    )
                ),
                gotoDetails = {}
            )
        }
    }
}