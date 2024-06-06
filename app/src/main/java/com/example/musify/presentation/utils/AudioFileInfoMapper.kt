package com.example.musify.presentation.utils

import android.media.MediaMetadataRetriever
import com.example.musify.presentation.viewmodels.AudioFileInfo
import java.io.File

fun File.toAudiFileInfo(): AudioFileInfo {
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(this.path)

    val artistName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
    val durationString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)

    val duration = durationString?.toLongOrNull() ?: 0

    val mediaImage = retriever.embeddedPicture

    retriever.release()

    return AudioFileInfo(
        fileName = this.name,
        artistName = artistName ?: "No Artist",
        filePath = this.path,
        fileSize = this.length(),
        duration = duration,
        mediaImage = mediaImage
    )
}