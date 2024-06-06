package com.example.musify.presentation.ui.playlist

import com.example.musify.presentation.viewmodels.AudioFileInfo

data class PlaylistUiState(
    val viewPlaylistId: Long = 0L,
    val viewPlaylistSongs: List<AudioFileInfo> = emptyList(),
    val currentPlaylistId: Long = 0L,
    val currentPlaylistSongs: List<AudioFileInfo> = emptyList(),
    val selectedSong: AudioFileInfo? = null,
)
