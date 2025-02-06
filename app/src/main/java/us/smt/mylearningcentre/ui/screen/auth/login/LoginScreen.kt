package us.smt.mylearningcentre.ui.screen.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import us.smt.mylearningcentre.ui.screen.auth.view.textview.LoginTextView
import us.smt.mylearningcentre.ui.screen.auth.view.textview.PassWordTextField
import us.smt.mylearningcentre.util.AuthError

class LoginScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = getViewModel<LoginViewModel>()
        val state by viewModel.state.collectAsState()
        LoginPage(
            state = state,
            onAction = viewModel::onAction
        )
    }

}

@Composable
private fun LoginPage(
    state: LoginState,
    onAction: (LoginIntent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Login",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.Black,
                fontSize = 28.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            LoginTextView(
                state = state.email,
                onChange = { email -> onAction(LoginIntent.EmailChanged(email)) }
            )

            PassWordTextField(
                state = state.password,
                onChange = { password -> onAction(LoginIntent.PasswordChanged(password)) }
            )
            Button(
                onClick = {
                    onAction(LoginIntent.Login)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Login", fontSize = 18.sp)
            }
            TextButton(
                onClick = {
                    onAction(LoginIntent.OpenRegistration)
                }
            ) {
                Text("Don't have an account? Register")
            }
            if (state.error != null) {
                Spacer(Modifier.height(24.dp))
                val message = when (state.error) {
                    AuthError.UserNotRegister -> "You must register first"
                    AuthError.UserNotFound -> "User not found"
                    else -> "Error"
                }
                Text(message, color = MaterialTheme.colorScheme.error)

            }
        }
    }

}

@Preview
@Composable
private fun LoginPagePrev() {
    LoginPage(LoginState()) {}
}