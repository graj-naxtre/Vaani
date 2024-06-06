package com.example.musify.presentation.ui.song

sealed class SongEvent {
    data object Pause : SongEvent()
    data object Resume : SongEvent()
    data object SkipToNextSong: SongEvent()
    data object SkipToPreviousSong: SongEvent()
    data class SeekTo(val position: Long) : SongEvent()
}