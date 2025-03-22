package com.coderbdk.budgetbuddy

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.coderbdk.budgetbuddy.data.repository.TransactionRepository
import com.coderbdk.budgetbuddy.data.repository.impl.DefaultTransactionRepository
import com.coderbdk.budgetbuddy.domain.budget.usecase.GetBudgetsUseCase
import com.coderbdk.budgetbuddy.domain.transaction.usecase.GetRecentTransactionsUseCase
import com.coderbdk.budgetbuddy.domain.transaction.usecase.GetTotalTransactionAmountUseCase
import com.coderbdk.budgetbuddy.ui.home.HomeViewModel

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.coderbdk.budgetbuddy", appContext.packageName)
    }

}