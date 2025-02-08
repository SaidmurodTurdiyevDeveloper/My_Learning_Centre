package us.smt.mylearningcentre.ui.screen.chat

import us.smt.mylearningcentre.data.model.MessageData
import us.smt.mylearningcentre.data.model.StudentData
import us.smt.mylearningcentre.ui.utils.TextFieldData
import us.smt.mylearningcentre.util.UserError

data class ChatState(
    val isLoading: Boolean = false,
    val messages: List<MessageData> = emptyList(),
    val error: UserError? = null,
    val user: StudentData? = null,
    val text: TextFieldData = TextFieldData()
)
