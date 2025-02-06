package us.smt.mylearningcentre.util

sealed class ResponseResult<T> {
    class Success<T>(val data: T) : ResponseResult<T>()
    class Error<T>(val error: UserError) : ResponseResult<T>()
}