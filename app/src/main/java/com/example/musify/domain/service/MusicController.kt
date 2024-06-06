package com.example.musify.domain.service

import com.example.musify.domain.other.PlayerState
import com.example.musify.presentation.viewmodels.AudioFileInfo
import kotlinx.coroutines.flow.Flow

interface MusicController {
    var mediaControllerCallback: ((
        playerState: PlayerState,
        currentSong: AudioFileInfo?,
        currentPosition: Long,
        totalDuration: Long,
        isShuffleEnabled: Boolean,
        isRepeatOnceEnabled: Boolean
    ) -> Unit)?

    fun addMediaItems(song: List<AudioFileInfo>)
    fun play(mediaItemIndex: Int)
    fun resume()
    fun pause()
    fun getCurrentPosition(): Flow<Long>
    fun skipToNextSong()
    fun skipToPreviousSong()
    fun getCurrentSong(): AudioFileInfo?
    fun seekTo(position: Long)
    fun destroy()
}