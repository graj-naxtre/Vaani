package com.example.musify.presentation.di

import com.example.musify.presentation.delegates.PlaylistUiStateDelegate
import com.example.musify.presentation.delegates.PlaylistUiStateDelegateApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PresentationModule {
    @Provides
    @Singleton
    fun providesPlaylistUiStateDelegate() : PlaylistUiStateDelegate {
        return PlaylistUiStateDelegateApp()
    }
}