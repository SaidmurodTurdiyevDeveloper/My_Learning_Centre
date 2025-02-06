package us.smt.mylearningcentre.util

sealed class PasswordError : TextViewError {
    data object NumberMissed : PasswordError()
    data object UpperLetterMissed : PasswordError()
    data object LowerLetterMissed : PasswordError()
    data object MoreLetterMissed : PasswordError()
    data object NotEnoughLetterMissed : PasswordError()
}
sealed class RePasswordError : TextViewError {
    data object IsSame : RePasswordError()
}

fun String.validatePassword(): TextViewError? {
    when {
        isBlank() -> return TextViewError.Empty
        none { it.isDigit() } -> return PasswordError.NumberMissed
        none { it.isUpperCase() } -> return PasswordError.UpperLetterMissed
        none { it.isLowerCase() } -> return PasswordError.LowerLetterMissed
        none { it.isLetter() } -> return PasswordError.MoreLetterMissed
        length < 8 -> return PasswordError.NotEnoughLetterMissed
    }
    return null
}
