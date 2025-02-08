package us.smt.mylearningcentre.util

interface UserError

fun getErrorMessage(error: UserError): String {
    return when (error) {
        is NetworkError.NetworkResult -> "Something went wrong, please try again"
        is NetworkError.InternetConnection<*> -> "Internet connection error, please check your connection"
        is AuthError.UserNotFound -> "User not found"
        is AuthError.UserNotRegister -> "User not registered"
        else -> "Unknown error"
    }
}
