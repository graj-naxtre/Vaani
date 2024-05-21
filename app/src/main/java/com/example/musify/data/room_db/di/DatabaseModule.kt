package com.example.musify.data.room_db.di

import android.content.Context
import androidx.room.Room
import com.example.musify.data.room_db.dao.PlaylistAndSongDao
import com.example.musify.data.room_db.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun providesDatabaseModule(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "musify_db").build()
    }

    @Singleton
    @Provides
    fun providesPlaylistSongDao(appDatabase: AppDatabase) : PlaylistAndSongDao{
        return appDatabase.getPlaylistSongDao()
    }
}