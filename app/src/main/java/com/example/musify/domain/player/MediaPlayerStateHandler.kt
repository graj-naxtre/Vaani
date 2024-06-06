package com.example.musify.domain.player

import android.util.Log
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player

class MediaPlayerStateHandler(val callback: (Player) -> Unit) : Player.Listener {

    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)
        when (playbackState) {
            Player.STATE_IDLE -> Log.d("MediaPlayerStateHandler", "playback state: IDLE")
            Player.STATE_ENDED -> Log.d("MediaPlayerStateHandler", "playback state: ENDED")
            Player.STATE_READY -> Log.d("MediaPlayerStateHandler", "playback state: READY")
            Player.STATE_BUFFERING -> Log.d("MediaPlayerStateHandler", "playback state: BUFFERING")
        }
    }

    override fun onEvents(player: Player, events: Player.Events) {
        super.onEvents(player, events)
        Log.d("MediaPlayerStateHandler", "isPlaying: ${player.isPlaying}")
        callback(player)
    }

    override fun onPlayerError(error: PlaybackException) {
        Log.e(
            "MediaPlayerStateHandler",
            "Player error: ${error.message} code:${error.errorCode} name:${error.errorCodeName}"
        )
        super.onPlayerError(error)
    }
}
