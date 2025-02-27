package com.coderbdk.budgetbuddy.ui.transaction.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun AlertDialogBudgetCreate(
    onDismissRequest: () -> Unit,
    goto: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Budget Required") },
        text = { Text("Please create a budget first before creating a transaction.") },
        confirmButton = {
            Button(onClick = {
                onDismissRequest()
                goto()
            }) {
                Text("Create Budget")
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}