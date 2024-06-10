package com.example.musify.presentation.ui.playlist

import com.example.musify.presentation.viewmodels.AudioFileInfo

data class PlaylistState(
    val viewPlaylistId: Long = 0L,
    val viewPlaylistName: String = "Playlist",
    val viewPlaylistSongs: List<AudioFileInfo> = emptyList(),
    val currentPlaylistId: Long = 0L,
    val currentPlaylistSongs: List<AudioFileInfo> = emptyList(),
    val selectedSong: AudioFileInfo? = null,
)
