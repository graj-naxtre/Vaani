package com.example.musify.data.room_db.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.musify.data.room_db.dao.PlaylistAndSongDao
import com.example.musify.data.room_db.entity.Playlist
import com.example.musify.data.room_db.entity.PlaylistSongCrossRef
import com.example.musify.data.room_db.entity.Song

@Database(entities = [Song::class, Playlist::class, PlaylistSongCrossRef::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getPlaylistSongDao() : PlaylistAndSongDao
}