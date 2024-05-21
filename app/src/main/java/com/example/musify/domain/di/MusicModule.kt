package com.example.musify.domain.di

import com.example.musify.domain.repository.MusicRepository
import com.example.musify.data.repository.MusicRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
}