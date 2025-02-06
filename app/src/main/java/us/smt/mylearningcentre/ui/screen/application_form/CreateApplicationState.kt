package us.smt.mylearningcentre.ui.screen.application_form

import us.smt.mylearningcentre.ui.utils.TextFieldData
import us.smt.mylearningcentre.util.UserError

data class CreateApplicationState(
    val loading: Boolean = false,
    val error: UserError? = null,
    val description: TextFieldData = TextFieldData()
)
