package com.coderbdk.budgetbuddy.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.coderbdk.budgetbuddy.data.db.entity.Budget
import com.coderbdk.budgetbuddy.data.db.entity.ExpenseCategory
import com.coderbdk.budgetbuddy.data.db.entity.Transaction
import com.coderbdk.budgetbuddy.data.model.BudgetPeriod
import com.coderbdk.budgetbuddy.data.model.BudgetWithCategory
import com.coderbdk.budgetbuddy.data.model.TransactionType
import com.coderbdk.budgetbuddy.data.model.TransactionWithBothCategories
import com.coderbdk.budgetbuddy.ui.theme.BudgetBuddyTheme
import com.coderbdk.budgetbuddy.ui.transaction.content.TransactionItem
import com.coderbdk.budgetbuddy.utils.TextUtils.capitalizeFirstLetter
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.absoluteValue

val dateFormatter by lazy {
    SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
}

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onNavigateToAddTransaction: () -> Unit,
    onNavigateToBudgets: () -> Unit,
    onNavigateToTransactions: () -> Unit,

    ) {

    val pagerState = rememberPagerState(initialPage = 1, pageCount = { 2 })

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                HorizontalPager(
                    state = pagerState
                ) { page ->
                    Box(Modifier.height(224.dp), contentAlignment = Alignment.Center) {
                        when (page) {
                            0 -> BalanceCard(
                                uiState.totalIncome,
                                uiState.totalExpense,
                                onNavigateToAddTransaction
                            )

                            1 -> ExpenseOverviewCard(uiState.recentTransactions)
                        }
                    }

                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(pagerState.pageCount) { iteration ->
                        val color =
                            if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                        Box(
                            modifier = Modifier
                                .padding(2.dp)
                                .clip(CircleShape)
                                .background(color)
                                .size(8.dp)
                        )
                    }
                }
            }

            BudgetProgressSection(uiState.budgets, onNavigateToBudgets)
            Spacer(modifier = Modifier.height(16.dp))
        }
        recentTransactionsSection(uiState.recentTransactions, onNavigateToTransactions)
    }
}

@Composable
fun BalanceCard(
    income: Double,
    expense: Double,
    onNavigateToAddTransaction: () -> Unit
) {
    val balance = income - expense
    val premiumGradient = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.tertiaryContainer
        )
    )

    Card(
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(premiumGradient)
                .padding(24.dp)
        ) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            ){


                Text(
                    text = buildAnnotatedString {
                        if(balance < 0 ) append("-")
                        withStyle(SpanStyle(baselineShift = BaselineShift.Superscript, fontSize = 18.sp)) {
                            append("$")
                        }
                        withStyle(SpanStyle()) {
                            append(String.format("%.2f", balance.absoluteValue))
                        }

                    },
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = if(balance < 0) MaterialTheme.colorScheme.onPrimaryContainer.copy(0.4f) else MaterialTheme.colorScheme.onPrimaryContainer
                )

                Text(
                    text = "TOTAL BALANCE",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IncomeExpenseItem(
                        title = "INCOME",
                        amount = income,
                        isIncome = true
                    )
                    IncomeExpenseItem(
                        title = "EXPENSE",
                        amount = expense,
                        isIncome = false
                    )
                }
            }
        }
    }
}

@Composable
private fun IncomeExpenseItem(title: String, amount: Double, isIncome: Boolean) {
    val icon = if (isIncome) Icons.Rounded.ArrowDownward else Icons.Rounded.ArrowUpward
    val iconBgColor = if (isIncome) Color(0xFF4CAF50).copy(0.2f) else Color(0xFFF44336).copy(0.2f)
    val iconColor = if (isIncome) Color(0xFF4CAF50) else Color(0xFFF44336)

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(iconBgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = "$${String.format("%.2f", amount)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun ExpenseOverviewCard(transactions: List<TransactionWithBothCategories>) {
    OutlinedCard(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier.padding(16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            ExpenseChart(transactions)
            Text(
                text = "Expense Breakdown",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
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
    val maxVisibleLabels = remember { 5 }

    val legendEntries = remember {
        categoryTotals.entries
            .sortedByDescending { it.value }
            .take(maxVisibleLabels)
            .map {
                LegendEntry(
                    it.key.toString(),
                    Legend.LegendForm.CIRCLE,
                    14f,
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
                setCenterTextSizePixels(18f)
                setHoleColor(colorScheme.secondaryContainer.toArgb())
                setCenterTextColor(colorScheme.onSecondaryContainer.toArgb())

                animateY(1000)
                setUsePercentValues(true)
                setExtraOffsets(0f, 0f, 50f, 5f)
                setTouchEnabled(false)

                legend.apply {
                    setCustom(legendEntries)
                    setDrawInside(false)
                    verticalAlignment = Legend.LegendVerticalAlignment.TOP
                    horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                    orientation = Legend.LegendOrientation.VERTICAL
                    textColor = colorScheme.onSurface.toArgb()
                    textSize = 12f

                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .padding(8.dp)
    )

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
            Column(modifier = Modifier) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        item.expenseCategory?.name?.lowercase()?.capitalizeFirstLetter()
                            ?: "Unknown",
                    )
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("$${item.budget.spentAmount}")
                            }
                            append("/$${item.budget.limitAmount}")
                        },
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { progress.coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(CircleShape),
                    color = Color(item.expenseCategory?.colorCode ?: Color.Gray.toArgb()),
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

        }
    }
}

@Composable
fun SectionHeader(title: String, onActionClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
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
        darkTheme = false
    ) {
        HomeScreen(
            uiState = HomeUiState(
                totalIncome = 10.0,
                totalExpense = 2.0,
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
                    ),
                    BudgetWithCategory(
                        budget = Budget(
                            expenseCategoryId = 0,
                            period = BudgetPeriod.DAILY,
                            limitAmount = 0.0,
                            startDate = 0L,
                            endDate = 0L
                        ),
                        expenseCategory = ExpenseCategory(name = "Rent")
                    )
                ),
            ),
            onNavigateToAddTransaction = {},
            onNavigateToBudgets = {},
            onNavigateToTransactions = {}
        )
    }
}