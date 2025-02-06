package us.smt.mylearningcentre.ui.designs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import us.smt.mylearningcentre.ui.utils.TextFieldData
import us.smt.mylearningcentre.util.getErrorText

@Composable
fun SimpleTextView(
    hint: String,
    state: TextFieldData,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = state.text,
        onValueChange = onChange,
        label = { Text(hint) },
        isError = state.error != null,
        supportingText = {
            if (state.error != null) {
                val errorText = getErrorText(state.error)
                Text(
                    text = errorText,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        shape = RoundedCornerShape(4.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}