package us.smt.mylearningcentre.ui.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun LeavingClubDialog(
    onDismiss: () -> Unit,
    leave: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Laving Club") },
        text = {
            Column {
                Text("Do you want to leave the club?")
            }
        },
        confirmButton = {
            Button(
                onClick = leave
            ) {
                Text("Leave")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("No")
            }
        }
    )
}

