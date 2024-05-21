package com.example.musify.domain.player

import android.content.Context
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.musify.presentation.view_model.AudioFileInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MyAudioPlayer @Inject constructor(
    val exoPlayer: ExoPlayer,
    val mediaPlayerStateHandler: MediaPlayerStateHandler,
) {
    companion object {
        private var instance: MyAudioPlayer? = null

        fun getInstance(): MyAudioPlayer {
            return instance!!
        }
    }

    init {
        instance = this
    }

    fun intializeExoPlayer() {
        // will think of it later
    }

    fun getCurrentPlaybackProgressFlow() = flow {
        if (!exoPlayer.isPlaying) {
            return@flow
        }
        while (exoPlayer.currentPosition <= exoPlayer.duration) {
            emit(exoPlayer.currentPosition)
            delay(30)
        }
        emit(exoPlayer.currentPosition)
        // when paused, the same value will be emitted, to prevent the
        // emission of the same value, use distinctUntilChanged
    }.distinctUntilChanged()

    fun setMediaItem(mediaItem: MediaItem) {
        try {
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.playWhenReady = false
        } catch (e: Exception) {
            Log.e("MediaItem", "$e")
        }
    }

    fun setMediaItemList(mediaItems: List<MediaItem>) {
        try {
            exoPlayer.setMediaItems(mediaItems)
            exoPlayer.prepare()
            exoPlayer.playWhenReady = false
        } catch (e: Exception) {

        }
    }

    fun setNewPlaybackPosition(value: Long) {
        exoPlayer.seekTo(value)
    }

    fun playSong(media: AudioFileInfo, songIndex: Int) {
        mediaPlayerStateHandler.let {
            it.songState.value = it.songState.value.copy(
                songName = media.fileName,
                artistName = media.artistName,
                songDuration = media.duration,
                songImage = media.mediaImage
            )
            it.playerState.value = PlaybackState.PLAYING
            it.currentSongIndex.value = songIndex
        }
        play()
    }

    fun play() {
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
        exoPlayer.play()
    }

    fun pause() {
        mediaPlayerStateHandler.playerState.value = PlaybackState.PAUSED
        exoPlayer.prepare()
        exoPlayer.pause()
    }

    fun playNextSong(songIndex: Int) {
        exoPlayer.seekToNextMediaItem()
    }

    fun playPrevSong(songIndex: Int) {
        exoPlayer.seekToPreviousMediaItem()
    }

    fun release() {
        if (exoPlayer.playbackState != Player.STATE_IDLE) {
            exoPlayer.seekTo(0)
            exoPlayer.playWhenReady = false
            exoPlayer.stop()
        }
        exoPlayer.clearMediaItems()
    }
}

/*
private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Musify Notification channel"
            val descriptionText = "Music Player Notification channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("Channel_id-1", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }
 */