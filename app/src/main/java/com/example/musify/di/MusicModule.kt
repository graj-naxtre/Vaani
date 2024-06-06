package com.example.musify.di

import android.content.Context
import com.example.musify.domain.repository.MusicRepository
import com.example.musify.data.repository.MusicRepositoryImpl
import com.example.musify.data.service.MusicControllerImpl
import com.example.musify.domain.service.MusicController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MusicModule {

    @Provides
    @Singleton
    fun provideMusicRepository(musicRepositoryImpl: MusicRepositoryImpl): MusicRepository {
        return musicRepositoryImpl
    }

    @Provides
    @Singleton
    fun provideMusicController(@ApplicationContext context: Context) : MusicController {
        return MusicControllerImpl(context)
    }
}