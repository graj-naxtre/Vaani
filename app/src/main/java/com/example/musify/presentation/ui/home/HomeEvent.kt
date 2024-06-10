package com.example.musify.presentation.ui.home

import com.example.musify.presentation.viewmodels.AudioFileInfo

sealed class HomeEvent {
    data object PlaySong: HomeEvent()
    data object PauseSong: HomeEvent()
    data object ResumeSong: HomeEvent()
    data object FetchSongs: HomeEvent()
    data class onFolderIndexChange(val index: Int) : HomeEvent()
    data class OnSongSelected(val selectedSong: AudioFileInfo): HomeEvent()
    data class AddSongToPlaylist(val songToAddInPlaylist: AudioFileInfo): HomeEvent()
}