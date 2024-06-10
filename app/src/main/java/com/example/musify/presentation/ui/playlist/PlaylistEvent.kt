package com.example.musify.presentation.ui.playlist

import com.example.musify.presentation.viewmodels.AudioFileInfo

sealed class PlaylistEvent {
    data object OnPlay : PlaylistEvent()
    data class OnSongSelected(val songSelected: AudioFileInfo) : PlaylistEvent()
    data class OnRemoveFromPlaylist(val playlistId: Long?, val filePath: String?) : PlaylistEvent()
    data class OnDeletePlaylist(val playlistId: Long?) : PlaylistEvent()
}