package us.smt.mylearningcentre.ui.screen.auth.login

sealed interface LoginIntent {
    data class EmailChanged(val email: String) : LoginIntent
    data class PasswordChanged(val password: String) : LoginIntent
    data object Login : LoginIntent
    data object OpenRegistration : LoginIntent
}