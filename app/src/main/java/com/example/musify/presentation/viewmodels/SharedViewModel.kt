package com.example.musify.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musify.domain.other.PlayerState
import com.example.musify.domain.service.MusicController
import com.example.musify.presentation.ui.song.SongEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private var musicController: MusicController) :
    ViewModel() {
    var musicControllerUiState by mutableStateOf(MusicControllerUiState())
        private set

    init {
        setMediaControllerCallback()
    }

    private fun setMediaControllerCallback() {
        musicController.mediaControllerCallback = { playerState: PlayerState,
                                                    currentSong: AudioFileInfo?,
                                                    currentPosition: Long,
                                                    totalDuration: Long,
                                                    isShuffleEnabled: Boolean,
                                                    isRepeatOnceEnabled: Boolean ->

            musicControllerUiState = musicControllerUiState.copy(
                playerState,
                currentSong,
                currentPosition,
                totalDuration,
                isShuffleEnabled,
                isRepeatOnceEnabled
            )

            if (playerState == PlayerState.PLAYING) {
                viewModelScope.launch {
                    musicController.getCurrentPosition().collect { currentPosition ->
                        musicControllerUiState =
                            musicControllerUiState.copy(currentPosition = currentPosition)
                    }
                }
            }
        }
    }

    fun onEvent(event: SongEvent) {
        when (event) {
            SongEvent.Pause -> musicController.pause()
            SongEvent.Resume -> musicController.resume()
            SongEvent.SkipToNextSong -> skipToNextSong()
            SongEvent.SkipToPreviousSong -> skipToPreviousSong()
            is SongEvent.SeekTo -> musicController.seekTo(event.position)
        }
    }

    private fun skipToNextSong() {
        musicController.skipToNextSong()
    }

    private fun skipToPreviousSong() {
        musicController.skipToPreviousSong()
    }

    fun destroyMediaController() {
        musicController.destroy()
    }
}