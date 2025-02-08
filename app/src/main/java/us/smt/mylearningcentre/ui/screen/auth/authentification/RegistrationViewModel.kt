package us.smt.mylearningcentre.ui.screen.auth.authentification

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import us.smt.mylearningcentre.data.model.CreateStudentData
import us.smt.mylearningcentre.domen.use_case.RegisterUseCase
import us.smt.mylearningcentre.ui.utils.AppNavigator
import us.smt.mylearningcentre.ui.utils.BaseViewModel
import us.smt.mylearningcentre.ui.utils.TextFieldData
import us.smt.mylearningcentre.util.RePasswordError
import us.smt.mylearningcentre.util.ResponseResult
import us.smt.mylearningcentre.util.TextViewError
import us.smt.mylearningcentre.util.isValidEmail
import us.smt.mylearningcentre.util.validatePassword
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    appNavigator: AppNavigator,
    private val useCase: RegisterUseCase
) : BaseViewModel<RegistrationState, RegistrationIntent>(initializeData = RegistrationState(), appNavigator = appNavigator) {
    override fun onAction(intent: RegistrationIntent) {
        when (intent) {
            is RegistrationIntent.NameChanged -> changeName(intent.name)
            is RegistrationIntent.LastNameChanged -> changeLastName(intent.surname)
            is RegistrationIntent.EmailChanged -> changeEmail(intent.email)
            is RegistrationIntent.PasswordChanged -> changePassword(intent.password)
            is RegistrationIntent.RePasswordChanged -> changeConfirmPassword(intent.password)
            RegistrationIntent.Back -> back()
            RegistrationIntent.Register -> register()
            RegistrationIntent.CloseError -> closeError()
        }
    }

    private fun closeError() {
        update(state = state.value.copy(error = null))
    }

    private fun changeName(name: String) {
        update(state = state.value.copy(name = TextFieldData(text = name, success = name.isBlank().not())))
    }

    private fun changeLastName(lastName: String) {
        update(state = state.value.copy(surname = TextFieldData(text = lastName, success = lastName.isBlank().not())))
    }

    private fun changeEmail(email: String) {
        val mailError = email.isValidEmail()
        update(state = state.value.copy(email = TextFieldData(text = email, success = mailError == null)))
    }

    private fun changePassword(password: String) {
        val passwordError = password.validatePassword()
        update(state = state.value.copy(password = TextFieldData(text = password, success = passwordError == null)))
    }

    private fun changeConfirmPassword(password: String) {
        val passwordError = password == state.value.password.text
        update(state = state.value.copy(confirmPassword = TextFieldData(text = password, success = passwordError)))
    }

    private fun register() {
        if (!state.value.name.success) {
            update(state = state.value.copy(name = state.value.name.copy(error = TextViewError.Empty)))
            return
        }
        if (!state.value.surname.success) {
            update(state = state.value.copy(surname = state.value.surname.copy(error = TextViewError.Empty)))
            return
        }
        if (!state.value.email.success) {
            val mailError = state.value.email.text.isValidEmail()
            update(state = state.value.copy(email = state.value.email.copy(error = mailError)))
            return
        }
        if (!state.value.password.success) {
            val passwordError = state.value.password.text.validatePassword()
            update(state = state.value.copy(password = state.value.password.copy(error = passwordError)))
            return
        }
        if (!state.value.confirmPassword.success) {
            update(state = state.value.copy(password = state.value.password.copy(error = RePasswordError.IsSame)))
            return
        }
        update(state = state.value.copy(isLoading = true))
        useCase.register(
            email = state.value.email.text, password = state.value.password.text, studentData = CreateStudentData(
                name = state.value.name.text,
                surname = state.value.surname.text
            )
        ).onEach { result ->
            when (result) {
                is ResponseResult.Success -> {
                    update(state = state.value.copy(isLoading = false))
                    if (result.data) {
                        back()
                    }
                }

                is ResponseResult.Error -> update(state = state.value.copy(error = result.error, isLoading = false))
            }
        }.launchIn(viewModelScope)
    }
}