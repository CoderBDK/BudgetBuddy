package com.coderbdk.budgetbuddy.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.coderbdk.budgetbuddy.data.db.entity.Budget
import com.coderbdk.budgetbuddy.data.db.entity.ExpenseCategory
import com.coderbdk.budgetbuddy.data.db.entity.Transaction
import com.coderbdk.budgetbuddy.data.model.BudgetPeriod
import com.coderbdk.budgetbuddy.data.model.BudgetWithCategory
import com.coderbdk.budgetbuddy.data.model.TransactionType
import com.coderbdk.budgetbuddy.data.model.TransactionWithBothCategories
import com.coderbdk.budgetbuddy.ui.navigation.Screen
import com.coderbdk.budgetbuddy.ui.theme.BudgetBuddyTheme
import com.coderbdk.budgetbuddy.ui.transaction.content.TransactionItem
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.text.SimpleDateFormat
import java.util.Locale

val dateFormatter by lazy {
    SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
}

@Composable
fun HomeScreen(
    totalIncome: Double,
    totalExpense: Double,
    recentTransactions: List<TransactionWithBothCategories>,
    budgets: List<BudgetWithCategory>,
    onNavigateToAddTransaction: () -> Unit,
    onNavigateToBudgets: () -> Unit,
    onNavigateToTransactions: () -> Unit,

) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            TotalBalanceCard(
                recentTransactions,
                totalIncome,
                totalExpense,
                onNavigateToAddTransaction
            )
            BudgetProgressSection(budgets, onNavigateToBudgets)
            Spacer(modifier = Modifier.height(16.dp))
        }
        recentTransactionsSection(recentTransactions, onNavigateToTransactions)
    }
}

@Composable
private fun ExpenseChart(transactions: List<TransactionWithBothCategories>) {
    if (transactions.isEmpty()) {
        return
    }

    val categoryTotals = remember {
        transactions
            .filter { it.transaction.type != TransactionType.INCOME }
            .groupBy { it.expenseCategory?.name }.mapValues { entry ->
                entry.value.sumOf { it.transaction.amount }
            }
    }
    val categoryIntColors = remember {
        transactions.associate {
            it.expenseCategory?.name to it.expenseCategory?.colorCode
        }
    }

    val entries = remember {
        categoryTotals.map { PieEntry(it.value.toFloat(), "", it.key ?: "") }
    }

    val pieColors = remember {
        entries.map { categoryIntColors[it.data] ?: Color(0xFFD4004A).toArgb() }
    }


    val dataSet = PieDataSet(entries, "Expenses").apply {
        this.colors = pieColors
        valueTextSize = 14f
        valueTextColor = Color.White.toArgb()
    }

    val pieData = remember {
        PieData(dataSet)
    }
    val maxVisibleLabels = remember { 2 }

    val legendEntries = remember {
        categoryTotals.entries
            .sortedByDescending { it.value }
            .take(maxVisibleLabels)
            .map {
                LegendEntry(
                    it.key.toString(),
                    Legend.LegendForm.SQUARE,
                    10f,
                    2f,
                    null,
                    categoryIntColors[it.key.toString()] ?: Color.Gray.toArgb()
                )
            }
    }
    val colorScheme = MaterialTheme.colorScheme
    AndroidView(
        factory = { context ->
            PieChart(context).apply {
                data = pieData
                description.isEnabled = false
                isDrawHoleEnabled = true
                centerText = "Total\n${categoryTotals.values.sum()}"
                setCenterTextSizePixels(24f)
                setHoleColor(colorScheme.secondaryContainer.toArgb())
                setCenterTextColor(colorScheme.onSecondaryContainer.toArgb())
                animateY(1000)
                setUsePercentValues(true)
                legend.apply {
                    setCustom(legendEntries)
                    verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                    horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
                    orientation = Legend.LegendOrientation.HORIZONTAL
                    textColor = colorScheme.onSurface.toArgb()
                    textSize = 8f
                }
            }
        },
        modifier = Modifier
            .size(148.dp)
            .padding(8.dp)
    )

}

@Composable
fun TotalBalanceCard(
    recentTransactions: List<TransactionWithBothCategories>,
    income: Double,
    expense: Double,
    navigateToAddTransaction: () -> Unit
) {
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {
            ExpenseChart(recentTransactions)
            FilledTonalIconButton(
                onClick = {
                    navigateToAddTransaction()
                },
            ) {
                Icon(Icons.Default.AddCircle, "transaction")
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.End
            ) {
                SummaryItem("Income", income, Color(0xFF4CAF50))
                SummaryItem("Expense", expense, Color(0xFFF44336))
                val balance = income - expense
                SummaryItem("Balance", balance, MaterialTheme.colorScheme.primary, isBold = true)
            }

        }
    }
}

@Composable
private fun SummaryItem(label: String, amount: Double, color: Color, isBold: Boolean = false) {
    Column(horizontalAlignment = Alignment.End) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = color)
        Text(
            text = "$${String.format("%.2f", amount)}",
            style = if (isBold) MaterialTheme.typography.titleLarge else MaterialTheme.typography.titleMedium,
            fontWeight = if (isBold) FontWeight.ExtraBold else FontWeight.Bold
        )
    }
}

@Composable
private fun BudgetProgressSection(
    budgets: List<BudgetWithCategory>,
    navigateTo: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        SectionHeader("Budget Overview", navigateTo)

        budgets.take(3).forEach { item ->
            val progress = if (item.budget.limitAmount > 0)
                (item.budget.spentAmount / item.budget.limitAmount).toFloat() else 0f

            Surface(
                tonalElevation = 2.dp,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(item.expenseCategory?.name ?: "Unknown", fontWeight = FontWeight.Medium)
                        Text("$${item.budget.spentAmount} / $${item.budget.limitAmount}", style = MaterialTheme.typography.bodySmall)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { progress.coerceIn(0f, 1f) },
                        modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                        color = Color(item.expenseCategory?.colorCode ?: Color.Gray.toArgb()),
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, onActionClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        TextButton(onClick = onActionClick) {
            Text("See All")
            Icon(Icons.AutoMirrored.Filled.ArrowForward, null, modifier = Modifier.size(16.dp))
        }
    }
}

private fun LazyListScope.recentTransactionsSection(
    transactions: List<TransactionWithBothCategories>,
    gotoTransactions: () -> Unit
) {
    item {
        SectionHeader("Recent Transactions", gotoTransactions, Modifier.padding(horizontal = 16.dp))
    }
    items(transactions.take(3)) { transaction ->
        TransactionItem(transaction) {}
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    val transactions = listOf(
        TransactionWithBothCategories(
            transaction = Transaction(
                type = TransactionType.EXPENSE,
                amount = 120.0,
                transactionDate = 16 * 10000
            ),
            incomeCategory = null,
            expenseCategory = ExpenseCategory(name = "Food")
        ),
        TransactionWithBothCategories(
            transaction = Transaction(
                type = TransactionType.EXPENSE,
                amount = 10.0,
                transactionDate = 16 * 10000
            ),
            incomeCategory = null,
            expenseCategory = ExpenseCategory(name = "Medical")
        )
    )
    BudgetBuddyTheme(
        darkTheme = true
    ) {
        HomeScreen(
            totalIncome = 0.0,
            totalExpense = 0.0,
            recentTransactions = transactions,
            budgets = listOf(
                BudgetWithCategory(
                    budget = Budget(
                        expenseCategoryId = 0,
                        period = BudgetPeriod.DAILY,
                        limitAmount = 0.0,
                        startDate = 0L,
                        endDate = 0L
                    ),
                    expenseCategory = ExpenseCategory(name = "Food")
                )
            ),
            onNavigateToAddTransaction = {},
            onNavigateToBudgets = {},
            onNavigateToTransactions = {}
        )
    }
}