package com.example.musify.domain.di

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSession
import com.example.musify.domain.player.MediaPlayerStateHandler
import com.example.musify.domain.player.MyAudioPlayer
import com.google.common.util.concurrent.ListenableFuture
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.UUID
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MediaModule {

    @Provides
    @Singleton
    fun provideAudioAttributes(): AudioAttributes = AudioAttributes.Builder()
        .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
        .setUsage(C.USAGE_MEDIA)
        .build()

    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun provideExoPlayer(
        @ApplicationContext context: Context,
        audioAttributes: AudioAttributes
    ): ExoPlayer {
        return ExoPlayer.Builder(context)
            .setAudioAttributes(audioAttributes, true)
            .setDeviceVolumeControlEnabled(true)
            .setTrackSelector(DefaultTrackSelector(context))
            .setHandleAudioBecomingNoisy(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideMediaSession(@ApplicationContext context: Context, player: ExoPlayer): MediaSession {
        return MediaSession.Builder(context, player).build()
    }

    @Provides
    @Singleton
    fun provideMediaController(
        @ApplicationContext context: Context,
        session: MediaSession
    ): ListenableFuture<MediaController> {
        return MediaController.Builder(context, session.token).buildAsync()
    }

    @Provides
    @Singleton
    fun providesMediaPlayerStateHandler(exoPlayer: ExoPlayer) : MediaPlayerStateHandler {
        return MediaPlayerStateHandler(exoPlayer)
    }

    @Provides
    @Singleton
    fun providesAudioPlayer(exoPlayer: ExoPlayer, mediaPlayerStateHandler: MediaPlayerStateHandler) : MyAudioPlayer {
        return MyAudioPlayer(exoPlayer, mediaPlayerStateHandler)
    }
}