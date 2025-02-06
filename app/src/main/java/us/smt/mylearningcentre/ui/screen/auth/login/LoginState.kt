package us.smt.mylearningcentre.ui.screen.auth.login

import us.smt.mylearningcentre.ui.utils.TextFieldData
import us.smt.mylearningcentre.util.UserError

data class LoginState(
    val isLoading: Boolean = false,
    val isUserCantCreate: Boolean = false,
    val error: UserError? = null,
    val email: TextFieldData = TextFieldData(),
    val password: TextFieldData = TextFieldData()
)
