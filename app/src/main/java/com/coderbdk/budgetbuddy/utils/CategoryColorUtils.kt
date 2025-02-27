package com.coderbdk.budgetbuddy.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.coderbdk.budgetbuddy.data.model.BudgetCategory

object CategoryColorUtils {

    val categoryIntColors: Map<String, Int> = BudgetCategory.entries.associate {
        it.name to getCategoryColor(it).toArgb()
    }

    val categoryColors: Map<String, Color> = BudgetCategory.entries.associate {
        it.name to getCategoryColor(it)
    }

    private fun getCategoryColor(category: BudgetCategory): Color {
        return when (category) {
            BudgetCategory.FOOD -> Color(0xFFFF5722)
            BudgetCategory.TRANSPORTATION -> Color(0xFF3F51B5)
            BudgetCategory.ENTERTAINMENT -> Color(0xFFFFC107)
            BudgetCategory.HEALTHCARE -> Color(0xFF4CAF50)
            BudgetCategory.EDUCATION -> Color(0xFF3F9DB5)
            BudgetCategory.RENT -> Color(0xFFC9A844)
            BudgetCategory.SAVINGS -> Color(0xFF9A4CAF)
            BudgetCategory.OTHERS -> Color(0xFF9E9E9E)
        }
    }

}
