package com.example.musify.presentation.ui.home

import com.example.musify.data.model.AppFolderWithSongs
import com.example.musify.presentation.viewmodels.AudioFileInfo

data class HomeUiState(
    val loading: Boolean? = true,
    val folderWithSongs: List<AppFolderWithSongs> = emptyList(),
    val currentFolderIndex: Int = 0,
    val selectedSong: AudioFileInfo? = null,
    val songToAddInPlaylist: AudioFileInfo? = null,
    val errorMessage: String? = null
)