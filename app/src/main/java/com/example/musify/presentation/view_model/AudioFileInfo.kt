package com.example.musify.presentation.view_model

import androidx.compose.runtime.Immutable

@Immutable
data class AudioFileInfo(
    val fileName: String,
    val artistName: String? = "No Artist",
    val filePath: String,
    val fileSize: Long,
    val duration: Long,
    val mediaImage: ByteArray?
)