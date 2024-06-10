package com.example.musify.presentation.ui.playlist

suspend fun <T : Any> launchAsync(execute: suspend () -> T): DatabaseCallState<T> {
    return try {
        val data = execute()
        DatabaseCallState.SUCCESS(data = data)
    } catch (e: Exception) {
        DatabaseCallState.ERROR("message: ${e.message}")
    }
}