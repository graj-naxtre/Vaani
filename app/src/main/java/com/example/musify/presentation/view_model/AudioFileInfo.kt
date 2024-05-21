package com.example.musify.presentation.view_model

import androidx.compose.runtime.Immutable
import com.example.musify.data.room_db.entity.Song

@Immutable
data class AudioFileInfo(
    val fileName: String,
    val artistName: String? = "No Artist",
    val filePath: String,
    val fileSize: Long,
    val duration: Long,
    val mediaImage: ByteArray?
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