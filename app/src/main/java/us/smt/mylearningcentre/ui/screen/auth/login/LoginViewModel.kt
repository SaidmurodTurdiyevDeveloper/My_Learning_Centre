package us.smt.mylearningcentre.ui.screen.auth.login

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import us.smt.mylearningcentre.domen.use_case.LoginUseCase
import us.smt.mylearningcentre.ui.screen.auth.authentification.RegistrationScreen
import us.smt.mylearningcentre.ui.screen.tab.MainTabScreen
import us.smt.mylearningcentre.ui.utils.AppNavigator
import us.smt.mylearningcentre.ui.utils.BaseViewModel
import us.smt.mylearningcentre.ui.utils.TextFieldData
import us.smt.mylearningcentre.util.ResponseResult
import us.smt.mylearningcentre.util.isValidEmail
import us.smt.mylearningcentre.util.validatePassword
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    appNavigator: AppNavigator,
    private val useCase: LoginUseCase
) :
    BaseViewModel<LoginState, LoginIntent>(initializeData = LoginState(), appNavigator = appNavigator) {
    override fun onAction(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.EmailChanged -> changeEmail(intent.email)
            is LoginIntent.PasswordChanged -> changePassword(intent.password)
            is LoginIntent.OpenRegistration -> openRegisterScreen()
            is LoginIntent.Login -> login()
        }
    }

    private fun changeEmail(email: String) {
        update(state = state.value.copy(email = TextFieldData(text = email, success = email.isValidEmail() == null)))
    }

    private fun changePassword(password: String) {
        update(state = state.value.copy(password = TextFieldData(text = password, success = password.validatePassword() == null)))
    }

    private fun openRegisterScreen() {
        update(state = state.value.copy(error = null))
        navigate(RegistrationScreen())
    }

    private fun login() {
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
        update(state = state.value.copy(isLoading = true))
        useCase.login(email = state.value.email.text, password = state.value.password.text).onEach { result ->
            when (result) {
                is ResponseResult.Error -> update(state = state.value.copy(error = result.error, isLoading = false))
                is ResponseResult.Success -> {
                    update(state = state.value.copy(isUserCantCreate = result.data, isLoading = false, error = null))
                    navigate(MainTabScreen())
                }
            }
        }.launchIn(viewModelScope)
    }

}