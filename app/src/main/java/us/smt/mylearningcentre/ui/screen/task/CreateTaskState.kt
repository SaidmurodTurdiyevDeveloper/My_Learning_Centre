package us.smt.mylearningcentre.ui.screen.task

import us.smt.mylearningcentre.ui.utils.TextFieldData
import us.smt.mylearningcentre.util.UserError

data class CreateTaskState(
    val isLoading: Boolean = false,
    val error: UserError? = null,
    val taskTitle: TextFieldData = TextFieldData(),
    val description: TextFieldData = TextFieldData()
)
