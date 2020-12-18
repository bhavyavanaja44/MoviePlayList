package com.bhavya.movielist.util

sealed class ViewState<T> {
    class Loading<T> : ViewState<T>()

    data class Success<T>(val data: T) : ViewState<T>()

    data class Error<T>(val message: String) : ViewState<T>()

    companion object {
        fun <T> loading() = Loading<T>()
        fun <T> success(data: T) = Success(data)
        fun <T> error(message: String) = Error<T>(message)
    }

}
