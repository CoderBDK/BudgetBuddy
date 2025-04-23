package com.coderbdk.budgetbuddy.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.coderbdk.budgetbuddy.data.model.DefaultExpenseCategory

object CategoryColorUtils {

    val categoryIntColors: Map<String, Int> = DefaultExpenseCategory.entries.associate {
        it.name to Color.Red.toArgb()
    }

    val categoryColors: Map<String, Color> = DefaultExpenseCategory.entries.associate {
        it.name to Color.Black
    }



}
