package com.coderbdk.budgetbuddy.ui.transaction.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coderbdk.budgetbuddy.data.db.entity.Transaction
import com.coderbdk.budgetbuddy.data.model.BudgetCategory
import com.coderbdk.budgetbuddy.data.model.BudgetPeriod
import com.coderbdk.budgetbuddy.data.model.TransactionType
import com.coderbdk.budgetbuddy.ui.home.dateFormatter
import com.coderbdk.budgetbuddy.ui.theme.BudgetBuddyTheme
import com.coderbdk.budgetbuddy.utils.CategoryColorUtils
import java.util.Date


@Composable
fun TransactionItem(transaction: Transaction) {
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
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionItemPreview() {
    BudgetBuddyTheme {
        Column (Modifier.padding(8.dp)) {
            TransactionItem(
                Transaction(
                    id = 0,
                    type = TransactionType.INCOME,
                    amount = 0.0,
                    category = BudgetCategory.FOOD,
                    period = BudgetPeriod.DAILY,
                    date = System.currentTimeMillis()
                )
            )
            TransactionItem(
                Transaction(
                    id = 0,
                    type = TransactionType.EXPENSE,
                    amount = 0.0,
                    category = BudgetCategory.FOOD,
                    period = BudgetPeriod.DAILY,
                    date = System.currentTimeMillis()
                )
            )
        }
    }
}