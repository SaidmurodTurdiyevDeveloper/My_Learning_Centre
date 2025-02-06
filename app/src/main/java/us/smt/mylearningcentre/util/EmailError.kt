package us.smt.mylearningcentre.util

import android.util.Patterns

sealed class EmailError : TextViewError {
    data object Invalid : EmailError()
}

fun String.isValidEmail(): TextViewError? {
    if (this.isEmpty()) return TextViewError.Empty
    if (!Patterns.EMAIL_ADDRESS.matcher(this).matches()) {
        return EmailError.Invalid
    }
    return null
}
