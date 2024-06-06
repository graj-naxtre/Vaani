package com.example.musify.data.utils

import androidx.media3.common.MediaItem
import com.example.musify.presentation.viewmodels.AudioFileInfo

fun MediaItem.toAudioFile() : AudioFileInfo {
    return AudioFileInfo(
        fileName = mediaMetadata.title.toString(),
        artistName = mediaMetadata.albumArtist.toString(),
        mediaImage = mediaMetadata.artworkData,
        filePath = "",
        fileSize = 0L,
        duration = 0L
    )
}