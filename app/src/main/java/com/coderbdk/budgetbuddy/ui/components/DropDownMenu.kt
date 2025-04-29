package com.coderbdk.budgetbuddy.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coderbdk.budgetbuddy.data.db.entity.ExpenseCategory
import com.coderbdk.budgetbuddy.data.model.DefaultExpenseCategory
import com.coderbdk.budgetbuddy.ui.theme.BudgetBuddyTheme
import com.coderbdk.budgetbuddy.utils.TextUtils.capitalizeFirstLetter

data class DropDownEntry<out T>(
    val title: String,
    val data: T
)

@Composable
fun <T> DropDownMenu(
    modifier: Modifier = Modifier,
    title: String,
    entries: List<DropDownEntry<T>>,
    selectedIndex: Int,
    trailingContent: @Composable (() -> Unit)? = null,
    onSelected: (T, Int) -> Unit
) {
    var expandMenu by remember { mutableStateOf(false) }

    OutlinedCard(
        onClick = {
            expandMenu = true
        },
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.height(56.dp),
    ) {
        ListItem(
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent
            ),
            leadingContent = {
                Icon(
                    if (expandMenu) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    "expand"
                )
            },
            headlineContent = {
                Text(
                    title,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            supportingContent = {
                Text(entries[selectedIndex].title, fontSize = 12.sp)
            },
            trailingContent = trailingContent
        )

        DropdownMenu(
            expanded = expandMenu,
            onDismissRequest = {
                expandMenu = false
            }
        ) {
            entries.forEachIndexed { index, dropDownEntry ->
                DropdownMenuItem(
                    text = { Text(dropDownEntry.title) },
                    onClick = {
                        onSelected(dropDownEntry.data, index)
                        expandMenu = false
                    }
                )
            }

        }

    }
}

private val categories = DefaultExpenseCategory.entries.map {
    DropDownEntry(
        title = it.name.lowercase().capitalizeFirstLetter(),
        data = it
    )
}

@Preview(showBackground = true)
@Composable
fun DropdownPreview() {
    var selectedIndex by remember { mutableIntStateOf(0) }

    BudgetBuddyTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            DropDownMenu(
                modifier = Modifier,
                title = "Choose Budget Category",
                entries = categories,
                selectedIndex = selectedIndex,
                onSelected = { data, index ->
                    selectedIndex = index
                }
            )
        }
    }
}