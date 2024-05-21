package com.example.musify.domain.repository

import com.example.musify.data.model.FolderWithSongs
import kotlinx.coroutines.flow.Flow
import java.io.File

interface MusicRepository {
    suspend fun getAllMediaSongs() : List<FolderWithSongs>

    suspend fun getAllAudioFolders() : List<String>

    fun getAllAudioFiles(path: String) : Flow<File>
}