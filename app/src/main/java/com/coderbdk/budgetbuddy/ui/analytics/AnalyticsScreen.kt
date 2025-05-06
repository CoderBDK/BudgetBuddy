package com.coderbdk.budgetbuddy.ui.analytics

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.coderbdk.budgetbuddy.data.db.entity.ExpenseCategory
import com.coderbdk.budgetbuddy.data.db.entity.IncomeCategory
import com.coderbdk.budgetbuddy.data.model.BudgetFilter
import com.coderbdk.budgetbuddy.data.model.BudgetPeriod
import com.coderbdk.budgetbuddy.data.model.BudgetWithCategory
import com.coderbdk.budgetbuddy.data.model.TransactionFilter
import com.coderbdk.budgetbuddy.data.model.TransactionType
import com.coderbdk.budgetbuddy.data.model.TransactionWithBothCategories
import com.coderbdk.budgetbuddy.ui.budget.AddBudgetDialog
import com.coderbdk.budgetbuddy.ui.budget.DatePickerModal
import com.coderbdk.budgetbuddy.ui.budget.convertMillisToDate
import com.coderbdk.budgetbuddy.ui.components.DropDownEntry
import com.coderbdk.budgetbuddy.ui.components.DropDownMenu
import com.coderbdk.budgetbuddy.ui.main.Screen
import com.coderbdk.budgetbuddy.ui.theme.BudgetBuddyTheme
import com.coderbdk.budgetbuddy.ui.transaction.TransactionUiEvent
import com.coderbdk.budgetbuddy.ui.transaction.TransactionUiState
import com.coderbdk.budgetbuddy.utils.TextUtils.capitalizeFirstLetter
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
    val budgets by viewModel.filteredBudgets.collectAsStateWithLifecycle(emptyList())
    val transactions by viewModel.filteredTransactions.collectAsStateWithLifecycle(emptyList())
    val expenseCategories by viewModel.expenseCategories.collectAsStateWithLifecycle()
    val incomeCategoryList by viewModel.incomeCategories.collectAsState(initial = emptyList())

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AnalyticsScreen(
        navController = navController,
        uiState = uiState,
        budgets = budgets,
        transactions = transactions,
        expenseCategoryList = expenseCategories,
        incomeCategoryList = incomeCategoryList,
        onBudgetFilter = viewModel::onBudgetFilter,
        onTransactionFilter = viewModel::onTransactionFilter
    )
}

@Composable
fun AnalyticsScreen(
    navController: NavController,
    uiState: AnalyticsUiState,
    budgets: List<BudgetWithCategory>,
    transactions: List<TransactionWithBothCategories>,
    expenseCategoryList: List<ExpenseCategory>,
    incomeCategoryList: List<IncomeCategory>,
    onBudgetFilter: (BudgetFilter?) -> Unit,
    onTransactionFilter: (TransactionFilter?) -> Unit
) {
    val analyticTypes = listOf(
        DropDownEntry("Budget", 0),
        DropDownEntry("Transaction", 1)
    )
    var selectedIndex by remember { mutableIntStateOf(0) }
    var showFilter by remember { mutableStateOf(false) }

    if (showFilter) {
        when (selectedIndex) {
            0 -> {
                BudgetFilterDialog(
                    budgetFilter = uiState.budgetFilter,
                    expenseCategoryList = expenseCategoryList,
                    onDismiss = {
                        showFilter = false
                    },
                    onSave = {
                        onBudgetFilter(it)
                        showFilter = false
                    },
                )
            }

            1 -> {
                TransactionFilterDialog(
                    navController = navController,
                    transactionFilter = uiState.transactionFilter,
                    expenseCategoryList = expenseCategoryList,
                    incomeCategoryList = incomeCategoryList,
                    onDismiss = {
                        showFilter = false
                    },
                    onSave = {
                        onTransactionFilter(it)
                        showFilter = false
                    },
                )
            }
        }
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        // Text("${budgets.size},${uiState.budgetFilter}")
        DropDownMenu(
            title = "Choose Analytic Type",
            entries = analyticTypes,
            selectedIndex = selectedIndex,
            trailingContent = {
                IconButton(
                    onClick = {
                        showFilter = true
                    }
                ) {
                    Icon(Icons.Default.FilterList, "filter")
                }
            },
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
        SimpleDateFormat(
            "MMM dd",
            Locale.getDefault()
        ).format(Date(transaction.transaction.transactionDate))
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

@Composable
fun BudgetFilterDialog(
    budgetFilter: BudgetFilter?,
    expenseCategoryList: List<ExpenseCategory>,
    onDismiss: () -> Unit,
    onSave: (BudgetFilter?) -> Unit,
) {

    var amount by remember { mutableStateOf(budgetFilter?.query) }
    var period by remember { mutableStateOf(budgetFilter?.period) }

    var selectedPeriodIndex by remember {
        mutableIntStateOf(
            budgetFilter?.period?.let { BudgetPeriod.valueOf(it.name).ordinal + 1 } ?: 0
        )
    }
    var selectedCategoryIndex by remember {
        mutableIntStateOf(
            expenseCategoryList.indexOfFirst { it.id == budgetFilter?.expenseCategoryId }
                .let { if (it == -1) 0 else it + 1 }
        )
    }


    var selectedStartDate by remember { mutableStateOf(budgetFilter?.startDate) }
    var selectedEndDate by remember { mutableStateOf(budgetFilter?.endDate) }

    var showModalState by remember { mutableIntStateOf(0) }
    var isClear by remember { mutableStateOf(false) }

    val expenseCategoryEntries by remember(expenseCategoryList) {
        derivedStateOf {
            buildList {
                add(DropDownEntry(title = "---", data = null))
                addAll(
                    expenseCategoryList.map { category ->
                        DropDownEntry(
                            title = category.name.lowercase().capitalizeFirstLetter(),
                            data = category
                        )
                    }
                )
            }
        }
    }

    val periodEntries by remember {
        derivedStateOf {
            buildList {
                add(DropDownEntry(title = "---", data = null))
                addAll(
                    BudgetPeriod.entries.map {
                        DropDownEntry(
                            title = it.name.lowercase().capitalizeFirstLetter(),
                            data = it
                        )
                    }
                )
            }
        }
    }

    if (showModalState == 1 || showModalState == 2) {
        DatePickerModal(
            onDateSelected = {
                if (showModalState == 1) selectedStartDate = it else selectedEndDate = it
            },
            onDismiss = { showModalState = 0 }
        )
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filter Budget") },
        text = {
            Column {
                OutlinedTextField(
                    value = amount ?: "",
                    onValueChange = { amount = it },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    ),
                    singleLine = true,
                    maxLines = 1
                )
                Spacer(Modifier.padding(4.dp))
                DropDownMenu(
                    modifier = Modifier,
                    title = "Choose Budget Category",
                    entries = expenseCategoryEntries,
                    selectedIndex = selectedCategoryIndex,
                    onSelected = { data, index ->
                        selectedCategoryIndex = index
                    }
                )
                Spacer(Modifier.padding(4.dp))
                DropDownMenu(
                    modifier = Modifier,
                    title = "Choose Budget Period",
                    entries = periodEntries,
                    selectedIndex = selectedPeriodIndex,
                    onSelected = { data, index ->
                        selectedPeriodIndex = index
                        period = data
                    }
                )

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                ) {
                    OutlinedTextField(
                        value = selectedStartDate?.let { convertMillisToDate(it) } ?: "",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            Icon(Icons.Default.DateRange, contentDescription = "Select date")
                        },
                        label = {
                            Text("From")
                        },
                        placeholder = { Text("--/--/----") },
                        textStyle = TextStyle(fontSize = 12.sp),
                        modifier = Modifier
                            .weight(1f)
                            .pointerInput(selectedStartDate) {
                                awaitEachGesture {
                                    awaitFirstDown(pass = PointerEventPass.Initial)
                                    val upEvent =
                                        waitForUpOrCancellation(pass = PointerEventPass.Initial)
                                    if (upEvent != null) {
                                        showModalState = 1
                                    }
                                }
                            }
                    )
                    Spacer(Modifier.width(2.dp))
                    OutlinedTextField(
                        value = selectedEndDate?.let { convertMillisToDate(it) } ?: "",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            Icon(Icons.Default.DateRange, contentDescription = "Select date")
                        },
                        label = {
                            Text("To")
                        },
                        placeholder = { Text("--/--/----") },
                        textStyle = TextStyle(fontSize = 12.sp),
                        modifier = Modifier
                            .weight(1f)
                            .pointerInput(selectedEndDate) {
                                awaitEachGesture {
                                    awaitFirstDown(pass = PointerEventPass.Initial)
                                    val upEvent =
                                        waitForUpOrCancellation(pass = PointerEventPass.Initial)
                                    if (upEvent != null) {
                                        showModalState = 2
                                    }
                                }
                            }
                    )
                }

            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (isClear) {
                        onSave(null)
                        return@Button
                    }
                    onSave(
                        BudgetFilter(
                            amount,
                            expenseCategoryEntries[selectedCategoryIndex].data?.id,
                            period,
                            selectedStartDate,
                            selectedEndDate
                        )
                    )
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                isClear = true
            }) {
                Text("Clear Filter")
            }
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }

        }
    )
}

@Composable
fun TransactionFilterDialog(
    navController: NavController,
    transactionFilter: TransactionFilter?,
    expenseCategoryList: List<ExpenseCategory>,
    incomeCategoryList: List<IncomeCategory>,
    onDismiss: () -> Unit,
    onSave: (TransactionFilter?) -> Unit

) {
    var amount by remember { mutableStateOf(transactionFilter?.query) }
    var period by remember { mutableStateOf(transactionFilter?.period) }
    var isRecurring by remember { mutableStateOf(transactionFilter?.isRecurring) }
    var selectedTypeIndex by remember {
        mutableIntStateOf(transactionFilter?.type?.let {
            TransactionType.valueOf(
                it.name
            ).ordinal
        } ?: 0)
    }

    val typeEntries = remember {
        TransactionType.entries.map {
            DropDownEntry(
                title = it.name.lowercase().capitalizeFirstLetter(),
                data = it
            )
        }
    }
    var selectedExpenseCategoryIndex by remember {
        mutableIntStateOf(
            expenseCategoryList.indexOfFirst { it.id == transactionFilter?.expenseCategoryId }
                .let { if (it == -1) 0 else it + 1 }
        )
    }
    var selectedIncomeCategoryIndex by remember {
        mutableIntStateOf(
            incomeCategoryList.indexOfFirst { it.id == transactionFilter?.incomeCategoryId }
                .let { if (it == -1) 0 else it + 1 }
        )
    }
    val expenseCategoryEntries by remember(expenseCategoryList) {
        derivedStateOf {
            buildList {
                add(DropDownEntry(title = "---", data = null))
                addAll(
                    expenseCategoryList.map { category ->
                        DropDownEntry(
                            title = category.name.lowercase().capitalizeFirstLetter(),
                            data = category
                        )
                    }
                )
            }
        }
    }

    val incomeCategoryEntries by remember(incomeCategoryList) {
        derivedStateOf {
            buildList {
                add(DropDownEntry(title = "---", data = null))
                addAll(
                    incomeCategoryList.map {
                        DropDownEntry(
                            title = it.name.lowercase().capitalizeFirstLetter(),
                            data = it
                        )
                    }
                )
            }
        }

    }

    var selectedPeriodIndex by remember {
        mutableIntStateOf(
            transactionFilter?.period?.let { BudgetPeriod.valueOf(it.name).ordinal + 1 } ?: 0
        )
    }
    val periodEntries by remember {
        derivedStateOf {
            buildList {
                add(DropDownEntry(title = "---", data = null))
                addAll(
                    BudgetPeriod.entries.map {
                        DropDownEntry(
                            title = it.name.lowercase().capitalizeFirstLetter(),
                            data = it
                        )
                    }
                )
            }
        }
    }

    var selectedStartDate by remember { mutableStateOf(transactionFilter?.startDate) }
    var selectedEndDate by remember { mutableStateOf(transactionFilter?.endDate) }

    var showModalState by remember { mutableIntStateOf(0) }
    var isClear by remember { mutableStateOf(false) }

    if (showModalState == 1 || showModalState == 2) {
        DatePickerModal(
            onDateSelected = {
                if (showModalState == 1) selectedStartDate = it else selectedEndDate = it
            },
            onDismiss = { showModalState = 0 }
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filter Transaction") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.padding(8.dp))
                OutlinedTextField(
                    value = amount ?: "",
                    onValueChange = { amount = it },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    ),
                    singleLine = true,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(Modifier.padding(8.dp))
                DropDownMenu(
                    modifier = Modifier,
                    title = "Transaction Type",
                    entries = typeEntries,
                    selectedIndex = selectedTypeIndex,
                    onSelected = { data, index ->
                        selectedTypeIndex = index
                    }
                )
                if (typeEntries[selectedTypeIndex].data == TransactionType.EXPENSE) {
                    Spacer(Modifier.padding(8.dp))

                    DropDownMenu(
                        modifier = Modifier,
                        title = "Transaction Category",
                        entries = expenseCategoryEntries,
                        selectedIndex = selectedExpenseCategoryIndex,
                        trailingContent = {
                            IconButton(
                                onClick = {
                                    navController.navigate(Screen.CategoryManage(TransactionType.EXPENSE))
                                }
                            ) {
                                Icon(Icons.Default.Settings, "manage")
                            }
                        },
                        onSelected = { data, index ->
                            selectedExpenseCategoryIndex = index
                        }
                    )
                    Spacer(Modifier.padding(8.dp))
                    DropDownMenu(
                        modifier = Modifier,
                        title = "Transaction Period",
                        entries = periodEntries,
                        selectedIndex = selectedPeriodIndex,
                        onSelected = { data, index ->
                            selectedPeriodIndex = index
                            period = data
                        }
                    )
                } else {
                    Spacer(Modifier.padding(8.dp))
                    DropDownMenu(
                        modifier = Modifier,
                        title = "Transaction Category",
                        entries = incomeCategoryEntries,
                        selectedIndex = selectedIncomeCategoryIndex,
                        trailingContent = {
                            IconButton(
                                onClick = {
                                    navController.navigate(Screen.CategoryManage(TransactionType.INCOME))
                                }
                            ) {
                                Icon(Icons.Default.Settings, "manage")
                            }
                        },
                        onSelected = { data, index ->
                            selectedIncomeCategoryIndex = index
                        }
                    )
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                ) {
                    OutlinedTextField(
                        value = selectedStartDate?.let { convertMillisToDate(it) } ?: "",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            Icon(Icons.Default.DateRange, contentDescription = "Select date")
                        },
                        label = {
                            Text("From")
                        },
                        placeholder = { Text("--/--/----") },
                        textStyle = TextStyle(fontSize = 12.sp),
                        modifier = Modifier
                            .weight(1f)
                            .pointerInput(selectedStartDate) {
                                awaitEachGesture {
                                    awaitFirstDown(pass = PointerEventPass.Initial)
                                    val upEvent =
                                        waitForUpOrCancellation(pass = PointerEventPass.Initial)
                                    if (upEvent != null) {
                                        showModalState = 1
                                    }
                                }
                            }
                    )
                    Spacer(Modifier.width(2.dp))
                    OutlinedTextField(
                        value = selectedEndDate?.let { convertMillisToDate(it) } ?: "",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            Icon(Icons.Default.DateRange, contentDescription = "Select date")
                        },
                        label = {
                            Text("To")
                        },
                        placeholder = { Text("--/--/----") },
                        textStyle = TextStyle(fontSize = 12.sp),
                        modifier = Modifier
                            .weight(1f)
                            .pointerInput(selectedEndDate) {
                                awaitEachGesture {
                                    awaitFirstDown(pass = PointerEventPass.Initial)
                                    val upEvent =
                                        waitForUpOrCancellation(pass = PointerEventPass.Initial)
                                    if (upEvent != null) {
                                        showModalState = 2
                                    }
                                }
                            }
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isRecurring ?: false,
                        onCheckedChange = { isRecurring = it }
                    )
                    Text(text = "Is Recurring")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (isClear) {
                        onSave(null)
                        return@Button
                    }
                    onSave(
                        if (typeEntries[selectedTypeIndex].data == TransactionType.INCOME) {
                            TransactionFilter(
                                amount,
                                typeEntries[selectedTypeIndex].data,
                                null,
                                incomeCategoryEntries[selectedIncomeCategoryIndex].data?.id,
                                null,
                                selectedStartDate,
                                selectedEndDate,
                                isRecurring
                            )
                        } else {
                            TransactionFilter(
                                amount,
                                typeEntries[selectedTypeIndex].data,
                                null,
                                expenseCategoryEntries[selectedExpenseCategoryIndex].data?.id,
                                period,
                                selectedStartDate,
                                selectedEndDate,
                                isRecurring
                            )
                        }

                    )
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                isClear = true
            }) {
                Text("Clear Filter")
            }
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }

        }
    )
}

@Preview(showBackground = true)
@Composable
fun AnalyticsPreview() {
    BudgetBuddyTheme {
        AnalyticsScreen(
            navController = rememberNavController(),
            uiState = AnalyticsUiState(),
            listOf(),
            listOf(),
            listOf(),
            listOf(),
            {}, {})
    }
}