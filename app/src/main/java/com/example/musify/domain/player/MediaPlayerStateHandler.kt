package com.example.musify.domain.player

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.musify.domain.model.SongDetails
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MediaPlayerStateHandler @Inject constructor(
    private val exoPlayer: ExoPlayer
) : Player.Listener {

    init {
        exoPlayer.addListener(this)
    }

    val playerState = mutableStateOf(PlaybackState.NONE)

    val songDuration = mutableStateOf(0L)
    val currentDuration = mutableStateOf(0L)
    val currentSongIndex = mutableStateOf(0)

    val songState = mutableStateOf(SongDetails())

    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)
        Log.d("listener","Media State handler")

        when (playbackState) {
            Player.STATE_READY -> {
                playerState.value = PlaybackState.READY
                songState.value.songDuration = exoPlayer.duration
            }

            Player.STATE_ENDED -> {
                playerState.value = PlaybackState.STOPPED
                songState.value.songDuration = 0L
            }

            Player.STATE_BUFFERING -> playerState.value = PlaybackState.BUFFERING

            Player.STATE_IDLE -> playerState.value = PlaybackState.NONE
        }
    }

    override fun onEvents(player: Player, events: Player.Events) {
        super.onEvents(player, events)
        songState.value.songDuration = player.duration.coerceAtLeast(0L)
        currentDuration.value = player.currentPosition.coerceAtLeast(0L)
        Log.d("mediaPlayer","${currentDuration.value}")
    }
}
