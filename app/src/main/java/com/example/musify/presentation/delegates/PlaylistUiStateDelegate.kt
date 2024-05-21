package com.example.musify.presentation.delegates

import kotlinx.coroutines.flow.StateFlow

interface PlaylistUiStateDelegate {
    val state: StateFlow<PlaylistUiState>
    suspend fun startLoading()

    suspend fun resetState()

    suspend fun errorOccurred(message: String)

}