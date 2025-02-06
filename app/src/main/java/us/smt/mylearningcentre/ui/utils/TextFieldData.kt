package us.smt.mylearningcentre.ui.utils

import us.smt.mylearningcentre.util.TextViewError


data class TextFieldData(
    val success: Boolean = false,
    val text: String = "",
    val error: TextViewError? = null
)
