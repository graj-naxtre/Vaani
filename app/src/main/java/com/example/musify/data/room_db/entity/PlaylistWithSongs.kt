package com.example.musify.data.room_db.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.musify.presentation.viewmodels.AudioFileInfo

@Entity
data class Playlist(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Long = 0L,
    @ColumnInfo(name = "playlist_name")
    val playlistName: String,
)

@Entity
data class Song(
    @PrimaryKey(autoGenerate = true)
    val mediaId: Long = 0L,
    @ColumnInfo(name = "media_name")
    val mediaName: String,
    @ColumnInfo(name = "media_artist_name")
    val mediaArtistName: String?,
    @ColumnInfo(name = "media_file_path")
    val mediaFilePath: String,
    @ColumnInfo(name = "media_file_size")
    val mediaFileSize: Long,
    @ColumnInfo(name = "media_duration")
    val mediaDuration: Long,
    @ColumnInfo(name = "media_image")
    val mediaImage: ByteArray?
){
    fun toAudioFileInfo() : AudioFileInfo {
        return AudioFileInfo(
            fileName = mediaName,
            artistName = mediaArtistName,
            mediaImage = mediaImage,
            duration = mediaDuration,
            filePath = mediaFilePath,
            fileSize = mediaFileSize
        )
    }
}

@Entity(primaryKeys = ["playlistId", "mediaId"])
data class PlaylistSongCrossRef(val playlistId: Long, val mediaId: Long)

data class PlaylistWithSongs(
    @Embedded val playlist: Playlist,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "mediaId",
        associateBy = Junction(PlaylistSongCrossRef::class)
    )
    val songs: List<Song>
)