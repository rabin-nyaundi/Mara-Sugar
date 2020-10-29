package com.rabitech.network

sealed class NetworkState<T> {
    class Loading<T> : NetworkState<T>()
    data class Success<T>(val data: T) : NetworkState<T>()
    data class Failed<T>(val exception: String) : NetworkState<T>()

    companion object {

        fun <T> loading() = Loading<T>()
        fun <T> success(data: T) = Success(data)
        fun <T> failed(exception: String) = Failed<T>(exception)
    }
}