package com.example.musify.presentation.viewmodels

import androidx.compose.runtime.Immutable
import com.example.musify.data.room_db.entity.Song

@Immutable
data class AudioFileInfo(
    val fileName: String,
    val artistName: String? = "No Artist",
    val mediaImage: ByteArray?,
    val duration: Long,
    val filePath: String,
    val fileSize: Long,
) {
    fun toSongMapper(): Song {
        return Song(
            mediaName = this.fileName,
            mediaArtistName = this.artistName,
            mediaFilePath = this.filePath,
            mediaFileSize = this.fileSize,
            mediaDuration = this.duration,
            mediaImage = this.mediaImage
        )
    }
}