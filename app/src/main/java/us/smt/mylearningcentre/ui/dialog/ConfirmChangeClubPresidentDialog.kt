package us.smt.mylearningcentre.ui.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import us.smt.mylearningcentre.data.model.StudentData

@Composable
fun ConfirmChangePresidentDialog(
    studentData: StudentData,
    onDismiss: () -> Unit,
    confirm: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Change President") },
        text = {
            Column {
                Text("Do you want to make ${studentData.name} the new president?")
            }
        },
        confirmButton = {
            Button(
                onClick = confirm
            ) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("No")
            }
        }
    )
}

