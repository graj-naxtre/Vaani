package com.example.musify.presentation.ui.playlist

sealed class DatabaseCallState<T : Any> {
    data class SUCCESS<T : Any>(val data: T) : DatabaseCallState<T>()
    data class ERROR<T : Any>(val message: String) : DatabaseCallState<T>()
}