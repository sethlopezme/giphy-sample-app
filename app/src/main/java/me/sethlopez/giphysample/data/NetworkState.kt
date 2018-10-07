package me.sethlopez.giphysample.data

sealed class NetworkState {
    object Loading : NetworkState()
    object Success : NetworkState()
    data class Failure(val cause: Throwable, val message: String? = null) : NetworkState()
}