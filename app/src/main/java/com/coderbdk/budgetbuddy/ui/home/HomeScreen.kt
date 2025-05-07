package com.coderbdk.budgetbuddy.ui.home

import androidx.compose.foundation.background
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
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
import com.coderbdk.budgetbuddy.ui.main.Screen
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
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    // val expenses by viewModel.expensesFlow.collectAsStateWithLifecycle(emptyList())
    // val incomes by viewModel.incomesFlow.collectAsStateWithLifecycle(emptyList())
    val budgets by viewModel.budgetsFlow.collectAsStateWithLifecycle(emptyList())
    val recentTransactions by viewModel.recentTransactionsFlow.collectAsStateWithLifecycle(emptyList())
    val totalExpense by viewModel.totalExpense.collectAsStateWithLifecycle(0.0)
    val totalIncome by viewModel.totalIncome.collectAsStateWithLifecycle(0.0)
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.initCategory(context)
    }
    HomeScreen(
        totalIncome = totalIncome,
        totalExpense = totalExpense,
        recentTransactions = recentTransactions,
        budgets = budgets,
    ) {
        navController.navigate(it)
    }
}

@Composable
fun HomeScreen(
    totalIncome: Double,
    totalExpense: Double,
    recentTransactions: List<TransactionWithBothCategories>,
    budgets: List<BudgetWithCategory>,
    navigateTo: (Screen) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            TotalBalanceCard(recentTransactions, totalIncome, totalExpense) {
                navigateTo(Screen.AddTransaction)
            }
            BudgetProgressSection(budgets) {
                navigateTo(Screen.Budgets)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        recentTransactionsSection(recentTransactions, gotoTransactionDetails = {
            navigateTo(Screen.Transactions)
        })
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
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Column(
                    Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Total Income", fontSize = 16.sp, color = Color(0xFF4CAF50))
                    Text("$${income}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                Icon(Icons.Default.ElectricBolt, "bolt", tint = Color(0xFFFF9800))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Total Expense", fontSize = 16.sp, color = Color(0xFFF44336))
                    Text("$${expense}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }


        }
    }
}

@Composable
private fun BudgetProgressSection(
    budgets: List<BudgetWithCategory>,
    navigateTo: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            Text(
                text = "Budget Overview",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
            IconButton(
                onClick = navigateTo
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, "icon")
            }
        }
        budgets.take(3).forEach { budgetWithCategory ->
            val budget = budgetWithCategory.budget
            val spentAmount = budget.spentAmount
            val totalBudget = budget.limitAmount
            val progress = (if (totalBudget > 0) spentAmount / totalBudget else 0f).toFloat()
            Column(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
                Text(
                    "${budgetWithCategory.expenseCategory?.name}: ${spentAmount}/${totalBudget}",
                    fontSize = 14.sp
                )
                val color = Color(budgetWithCategory.expenseCategory?.colorCode?:0xFFFFFFF)
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = color //if (progress > 1f) Color.Red.copy(0.6f) else Color.Blue.copy(0.6f)
                )
            }
        }
    }
}


private fun LazyListScope.recentTransactionsSection(
    transactions: List<TransactionWithBothCategories>,
    gotoTransactionDetails: () -> Unit
) {
    item {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            Text(
                text = "Recent Transactions",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
            IconButton(
                onClick = gotoTransactionDetails
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, "icon")
            }
        }

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
            navigateTo = {}
        )
    }
}