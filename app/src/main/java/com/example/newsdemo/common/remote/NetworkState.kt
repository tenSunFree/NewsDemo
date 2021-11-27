package com.example.newsdemo.common.remote

sealed class NetworkState<T>(val data: T? = null, val message: String? = null) {
    class LoadingNetworkState<T> : NetworkState<T>()
    class SuccessNetworkState<T>(data: T) : NetworkState<T>(data, null)

    @Suppress("UNUSED_PARAMETER")
    class ErrorNetworkState<T>(data: T? = null, message: String) : NetworkState<T>(data, message)
}