package com.coderbdk.budgetbuddy.ui.analytics

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.coderbdk.budgetbuddy.data.model.BudgetWithCategory
import com.coderbdk.budgetbuddy.data.model.TransactionType
import com.coderbdk.budgetbuddy.data.model.TransactionWithBothCategories
import com.coderbdk.budgetbuddy.ui.components.DropDownEntry
import com.coderbdk.budgetbuddy.ui.components.DropDownMenu
import com.coderbdk.budgetbuddy.ui.theme.BudgetBuddyTheme
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AnalyticsScreen(
    navController: NavController,
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val budgets by viewModel.budgetsFlow.collectAsStateWithLifecycle(emptyList())
    val transactions by viewModel.transactionsFlow.collectAsStateWithLifecycle(emptyList())
    AnalyticsScreen(budgets = budgets, transactions = transactions)
}

@Composable
fun AnalyticsScreen(
    budgets: List<BudgetWithCategory>,
    transactions: List<TransactionWithBothCategories>
) {
    val analyticTypes = listOf(
        DropDownEntry("Budget", 0),
        DropDownEntry("Transaction", 1)
    )
    var selectedIndex by remember { mutableIntStateOf(0) }
    Column(
        Modifier.fillMaxSize().padding(8.dp)
    ) {
        DropDownMenu(
            title = "Choose Analytic Type",
            entries = analyticTypes,
            selectedIndex = selectedIndex,
            onSelected = { index, data ->
                selectedIndex = index
            })

        AnimatedVisibility(selectedIndex == 0 && budgets.isNotEmpty()) {
            BudgetAnalytics(budgets)
        }
        if (selectedIndex == 1 && transactions.isNotEmpty()) {
            TransactionAnalytics(transactions)
        }


    }
}

fun composeColorToAndroidColor(composeColor: Color): Int {
    // Get the ARGB components from the Jetpack Compose Color
    val alpha = (composeColor.alpha * 255).toInt()
    val red = (composeColor.red * 255).toInt()
    val green = (composeColor.green * 255).toInt()
    val blue = (composeColor.blue * 255).toInt()

    // Return an Android color using ARGB
    return android.graphics.Color.argb(alpha, red, green, blue)
}
@Composable
fun BudgetAnalytics(list: List<BudgetWithCategory>) {
    val colorScheme = MaterialTheme.colorScheme
    val spentColor = colorScheme.error
    val remainingColor = colorScheme.primary
    val axisTextColor = colorScheme.secondary

    val entries = list.mapIndexed { index, budget ->
        BarEntry(
            index.toFloat(),
            floatArrayOf(
                budget.budget.spentAmount.toFloat(),
                (budget.budget.limitAmount - budget.budget.spentAmount).toFloat().coerceAtLeast(0f)
            )
        )
    }

    val dataSet = BarDataSet(entries, "Budgets").apply {
        setColors(
            composeColorToAndroidColor(spentColor),
            composeColorToAndroidColor(remainingColor)
        )
        valueTextColor = composeColorToAndroidColor(axisTextColor)
        stackLabels = arrayOf("Spent", "Remaining")
        valueTextSize = 10f
    }

    val barData = BarData(dataSet).apply {
        barWidth = 0.5f
    }

    val xLabels = list.map { budget ->
        SimpleDateFormat("MMM dd", Locale.getDefault()).format(Date(budget.budget.startDate))
    }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(300.dp)
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                BarChart(context).apply {
                    this.data = barData

                    xAxis.apply {
                        valueFormatter = IndexAxisValueFormatter(xLabels)
                        textColor = composeColorToAndroidColor(axisTextColor)
                        position = XAxis.XAxisPosition.BOTTOM
                        setDrawGridLines(false)
                        granularity = 1f
                        labelRotationAngle = -45f
                    }

                    axisLeft.apply {
                        textColor = composeColorToAndroidColor(axisTextColor)
                        setDrawGridLines(true)
                    }

                    axisRight.isEnabled = false

                    description.isEnabled = false
                    legend.apply {
                        isEnabled = true
                        textColor = composeColorToAndroidColor(axisTextColor)
                    }

                    isDragEnabled = true
                    setScaleEnabled(true)
                    setPinchZoom(false)
                    setVisibleXRangeMaximum(10f)

                    animateY(800)

                    invalidate()
                }
            },
            update = { chart ->
                chart.data = barData
                chart.xAxis.valueFormatter = IndexAxisValueFormatter(xLabels)
                chart.notifyDataSetChanged()
                chart.invalidate()
            }
        )
    }
}

@Composable
fun TransactionAnalytics(list: List<TransactionWithBothCategories>) {
    val colorScheme = MaterialTheme.colorScheme
    val incomeColor = colorScheme.primary
    val expenseColor = colorScheme.error
    val axisTextColor = colorScheme.secondary

    val entries = list.mapIndexed { index, transaction ->
        BarEntry(
            index.toFloat(),
            transaction.transaction.amount.toFloat()
        )
    }

    val dataSet = BarDataSet(entries, "Transactions").apply {
        colors = list.map { transaction ->
            if (transaction.transaction.type == TransactionType.INCOME) {
                composeColorToAndroidColor(incomeColor)
            } else {
                composeColorToAndroidColor(expenseColor)
            }
        }
        valueTextColor = composeColorToAndroidColor(axisTextColor)
        valueTextSize = 10f

    }

    val barData = BarData(dataSet).apply {
        barWidth = 0.5f
    }

    val xLabels = list.map { transaction ->
        SimpleDateFormat("MMM dd", Locale.getDefault()).format(Date(transaction.transaction.transactionDate))
    }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(300.dp)
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                BarChart(context).apply {
                    this.data = barData

                    xAxis.apply {
                        valueFormatter = IndexAxisValueFormatter(xLabels)
                        textColor = composeColorToAndroidColor(axisTextColor)
                        position = XAxis.XAxisPosition.BOTTOM
                        setDrawGridLines(false)
                        granularity = 1f
                        labelRotationAngle = -45f
                    }

                    axisLeft.apply {
                        textColor = composeColorToAndroidColor(axisTextColor)
                        setDrawGridLines(true)
                    }

                    axisRight.isEnabled = false

                    description.isEnabled = false
                    legend.isEnabled = false

                    isDragEnabled = true
                    setScaleEnabled(true)
                    setPinchZoom(false)
                    setVisibleXRangeMaximum(33f)

                    animateY(800)

                    invalidate()
                }
            },
            update = { chart ->
                chart.data = barData
                chart.xAxis.valueFormatter = IndexAxisValueFormatter(xLabels)
                chart.notifyDataSetChanged()
                chart.invalidate()
            }
        )
    }
}




@Preview(showBackground = true)
@Composable
fun AnalyticsPreview() {
    BudgetBuddyTheme {
        AnalyticsScreen(listOf(), listOf())
    }
}