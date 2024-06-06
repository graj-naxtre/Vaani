package com.example.musify.presentation.ui.search

import com.example.musify.presentation.viewmodels.AudioFileInfo

sealed class SearchEvent {
    data class OnSearch(val input: String) : SearchEvent()
    data class OnItemClick(val audioFileInfo: AudioFileInfo) : SearchEvent()
}
