package com.example.musify.presentation.viewmodels

import com.example.musify.domain.other.PlayerState

data class MusicControllerUiState(
    val playerState: PlayerState? = PlayerState.STOPPED,
    val currentSong: AudioFileInfo? = null,
    val currentPosition: Long = 0L,
    val totalDuration: Long = 0L,
    val isShuffleEnabled: Boolean = false,
    val isRepeatOnceEnabled: Boolean = false
)
