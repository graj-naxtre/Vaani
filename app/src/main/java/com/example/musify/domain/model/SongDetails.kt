package com.example.musify.domain.model

data class SongDetails(
    val songName: String = "Song Name",
    val artistName: String? = "Unknown",
    var songDuration: Long = 0L,
    val songImage: ByteArray? = null
)
