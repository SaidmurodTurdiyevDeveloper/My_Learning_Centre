package us.smt.mylearningcentre.ui.screen.auth.authentification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import us.smt.mylearningcentre.ui.designs.SimpleTextView
import us.smt.mylearningcentre.ui.screen.auth.view.textview.LoginTextView
import us.smt.mylearningcentre.ui.screen.auth.view.textview.PassWordTextField
import us.smt.mylearningcentre.util.getErrorMessage

class RegistrationScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = getViewModel<RegistrationViewModel>()
        val state by viewModel.state.collectAsState()
        RegistrationScreen(
            state = state,
            onAction = viewModel::onAction
        )
    }

}

@Composable
private fun RegistrationScreen(
    state: RegistrationState,
    onAction: (RegistrationIntent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (state.error != null) {
            Column (modifier = Modifier.align(Alignment.Center)) {
                Text(
                    text = getErrorMessage(state.error)
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = { onAction(RegistrationIntent.Back) }) {
                    Text(text = "Close")
                }
            }
        } else {
            IconButton(
                onClick = { onAction(RegistrationIntent.Back) },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(y = 16.dp)
                    .padding(16.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(16.dp)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Registration",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.Black,
                    fontSize = 28.sp,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
                SimpleTextView(
                    state = state.name,
                    hint = "Name",
                    onChange = { name -> onAction(RegistrationIntent.NameChanged(name)) }
                )
                SimpleTextView(
                    state = state.surname,
                    hint = "Surname",
                    onChange = { surname -> onAction(RegistrationIntent.LastNameChanged(surname)) }
                )
                LoginTextView(
                    state = state.email,
                    onChange = { email -> onAction(RegistrationIntent.EmailChanged(email)) }
                )
                PassWordTextField(
                    state = state.password,
                    onChange = { password -> onAction(RegistrationIntent.PasswordChanged(password)) }
                )
                PassWordTextField(
                    state = state.confirmPassword,
                    onChange = { password -> onAction(RegistrationIntent.RePasswordChanged(password)) }
                )
                Button(
                    onClick = {
                        onAction(RegistrationIntent.Register)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Text("Register", fontSize = 18.sp)
                }
            }
        }
    }
}

@Preview
@Composable
private fun RegistrationScreenPrev() {
    RegistrationScreen(RegistrationState()) {}
}