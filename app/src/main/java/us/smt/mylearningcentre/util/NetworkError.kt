package us.smt.mylearningcentre.util

sealed class NetworkError : UserError {
    data class InternetConnection<T>(val data:T?=null) : NetworkError()
    data object NetworkResult : NetworkError()
}