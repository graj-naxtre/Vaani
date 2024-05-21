package com.example.musify.presentation.delegates

sealed class PlaylistUiState {
    data object IDLE : PlaylistUiState()
    data object LOADING : PlaylistUiState()
    data class  ERROR(val errorMessage: String) : PlaylistUiState()
}