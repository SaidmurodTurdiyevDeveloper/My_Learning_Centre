package us.smt.mylearningcentre.ui.screen.auth.authentification

import us.smt.mylearningcentre.ui.utils.TextFieldData
import us.smt.mylearningcentre.util.UserError

data class RegistrationState(
    val isLoading: Boolean = false,
    val error: UserError? = null,
    val name: TextFieldData = TextFieldData(),
    val surname: TextFieldData = TextFieldData(),
    val email: TextFieldData = TextFieldData(),
    val password: TextFieldData = TextFieldData(),
    val confirmPassword: TextFieldData = TextFieldData()
)
