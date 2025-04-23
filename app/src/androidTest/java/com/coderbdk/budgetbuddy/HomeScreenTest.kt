package com.coderbdk.budgetbuddy

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.coderbdk.budgetbuddy.ui.main.BudgetBuddyApp
import com.coderbdk.budgetbuddy.ui.main.MainActivity
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import kotlin.math.max

@HiltAndroidTest
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testButton() {
        var clickCount = 0
        val maxClick = 5
        composeTestRule.setContent {
            var count by remember { mutableStateOf(0) }
            Button(
                onClick = {
                    count = count.plus(2)
                   clickCount = clickCount.inc()
                }
            ) {
                Text("Click Me")
            }
            Text("Count:${count}")
        }
        repeat(maxClick) {
            composeTestRule
                .onNodeWithText("Click Me")
                .performClick()
        }
        composeTestRule.onNodeWithText("Count:${maxClick * 2}").assertIsDisplayed()
        assertEquals(maxClick, clickCount)
    }
}
