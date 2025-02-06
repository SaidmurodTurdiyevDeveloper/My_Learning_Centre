package us.smt.mylearningcentre.ui.screen.auth.authentification

sealed interface RegistrationIntent {
    data class NameChanged(val name: String) : RegistrationIntent
    data class LastNameChanged(val surname: String) : RegistrationIntent
    data class EmailChanged(val email: String) : RegistrationIntent
    data class PasswordChanged(val password: String) : RegistrationIntent
    data class RePasswordChanged(val password: String) : RegistrationIntent
    data object Register : RegistrationIntent
    data object Back : RegistrationIntent
}