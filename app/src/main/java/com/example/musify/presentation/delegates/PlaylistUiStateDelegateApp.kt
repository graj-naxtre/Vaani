package com.example.musify.presentation.delegates

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlaylistUiStateDelegateApp : PlaylistUiStateDelegate {
    private val _state = MutableStateFlow<PlaylistUiState>(value = PlaylistUiState.IDLE)
    override val state: StateFlow<PlaylistUiState>
        get() = _state.asStateFlow()

    override suspend fun startLoading() {
        _state.emit(PlaylistUiState.LOADING)
    }

    override suspend fun resetState() {
        _state.emit(PlaylistUiState.IDLE)
    }

    override suspend fun errorOccurred(message: String) {
        _state.emit(PlaylistUiState.ERROR(errorMessage = message))
    }
}