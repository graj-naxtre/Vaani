package com.example.musify.domain.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.example.musify.domain.player.MyAudioPlayer
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlaybackService : MediaSessionService() {
    private var myMediaSession: MediaSession? = null

    @Inject
    lateinit var mediaSession: MediaSession

    @Inject
    lateinit var myAudioPlayer: MyAudioPlayer

    // Create your player and media session in the onCreate lifecycle event
    override fun onCreate() {
        super.onCreate()
        myMediaSession = mediaSession
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel("1234", "Musify App", NotificationManager.IMPORTANCE_HIGH)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // The user dismissed the app from the recent tasks
    override fun onTaskRemoved(rootIntent: Intent?) {
        val player = mediaSession.player
        if (!player.playWhenReady || player.mediaItemCount == 0) {
            // Stop the service if not playing, continue playing in the background
            // otherwise.
            stopForegroundNotificationService(this)
            stopSelf()
        }
    }

    override fun onGetSession(p0: MediaSession.ControllerInfo): MediaSession = mediaSession

    // Remember to release the player and media session in onDestroy
    override fun onDestroy() {
        myMediaSession?.run {
            myAudioPlayer.release()
            release()
        }
        super.onDestroy()
    }

    private fun stopForegroundNotificationService(mediaSessionService: MediaSessionService) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mediaSessionService.stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            @Suppress("DEPRECATION")
            mediaSessionService.stopForeground(true)
        }
    }
}
