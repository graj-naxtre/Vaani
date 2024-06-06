package com.example.musify.data.service

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.musify.data.utils.toAudioFile
import com.example.musify.domain.other.PlayerState
import com.example.musify.domain.player.MediaPlayerStateHandler
import com.example.musify.domain.service.MusicController
import com.example.musify.presentation.viewmodels.AudioFileInfo
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow

class MusicControllerImpl(
    context: Context
) : MusicController {
    private var mediaControllerFuture: ListenableFuture<MediaController>
    private val mediaController: MediaController?
        get() = if (mediaControllerFuture.isDone) mediaControllerFuture.get() else null

    init {
        val sessionToken =
            SessionToken(context, ComponentName(context, PlaybackService::class.java))
        mediaControllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        mediaControllerFuture.addListener({ controllerListener() }, MoreExecutors.directExecutor())
    }

    override var mediaControllerCallback: ((
        playerState: PlayerState,
        currentSong: AudioFileInfo?,
        currentPosition: Long,
        totalDuration: Long,
        isShuffleEnabled: Boolean,
        isRepeatOnceEnabled: Boolean
    ) -> Unit)? = null

    private fun controllerListener() {
        mediaController?.addListener(MediaPlayerStateHandler { player ->
            with(player) {
                mediaControllerCallback?.invoke(
                    playbackState.toPlayerState(isPlaying),
                    currentMediaItem?.toAudioFile(),
                    currentPosition.coerceAtLeast(0L),
                    duration.coerceAtLeast(0L),
                    shuffleModeEnabled,
                    repeatMode == Player.REPEAT_MODE_ONE
                )
            }
        })
    }

    private fun Int.toPlayerState(isPlaying: Boolean) =
        when (this) {
            Player.STATE_IDLE -> PlayerState.STOPPED
            Player.STATE_ENDED -> PlayerState.STOPPED
            else -> if (isPlaying) PlayerState.PLAYING else PlayerState.PAUSED
        }


    override fun addMediaItems(song: List<AudioFileInfo>) {
        val mediaItems = song.map {
            MediaItem.Builder()
                .setUri(it.filePath)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(it.fileName)
                        .setAlbumArtist(it.artistName)
                        .setDisplayTitle(it.fileName)
                        .setArtworkData(
                            it.mediaImage,
                            MediaMetadata.PICTURE_TYPE_FRONT_COVER
                        )
                        .build()
                ).build()
        }
        mediaController?.setMediaItems(mediaItems)
    }

    override fun play(mediaItemIndex: Int) {
        mediaController?.apply {
            seekToDefaultPosition(mediaItemIndex)
            prepare()
            this.play()
        }
    }

    override fun resume() {
        mediaController?.play()
    }

    override fun pause() {
        mediaController?.pause()
    }

    override fun getCurrentPosition() = flow {
        with(mediaController!!) {
            if (!isPlaying) {
                return@flow
            }
            while (currentPosition <= duration) {
                emit(currentPosition)
                delay(30)
            }
            emit(currentPosition)
            // when paused, the same value will be emitted, to prevent the
            // emission of the same value, use distinctUntilChanged
        }
    }.distinctUntilChanged()

    override fun skipToNextSong() {
        mediaController?.seekToNext()
    }

    override fun skipToPreviousSong() {
        mediaController?.seekToPrevious()
    }

    override fun getCurrentSong(): AudioFileInfo? {
        return mediaController?.currentMediaItem?.toAudioFile()
    }

    override fun seekTo(position: Long) {
        mediaController?.seekTo(position)
    }

    override fun destroy() {
        MediaController.releaseFuture(mediaControllerFuture)
        mediaControllerCallback = null
    }
}