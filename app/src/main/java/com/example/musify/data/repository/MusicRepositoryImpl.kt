package com.example.musify.data.repository

import android.content.Context
import android.provider.MediaStore
import com.example.musify.data.model.FolderWithSongs
import com.example.musify.domain.repository.MusicRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import java.io.File
import javax.inject.Inject

class MusicRepositoryImpl @Inject constructor(@ApplicationContext val context: Context) :
    MusicRepository {
    private var _allSongsWithFolder = mutableListOf<FolderWithSongs>()
    private val allSongsWithFolder: List<FolderWithSongs> get() = _allSongsWithFolder

    override suspend fun getAllMediaSongs() : List<FolderWithSongs> {
        val listOfMediaFolderPaths: List<String> = getAllAudioFolders()
        listOfMediaFolderPaths.forEach { folderPath ->
            val folderWithSongs = FolderWithSongs(
                songFolder = folderPath.split("/").last(),
                songFiles = getAllAudioFiles(folderPath).toList()
            )
            _allSongsWithFolder.add(folderWithSongs)
        }

        return allSongsWithFolder
    }

    override suspend fun getAllAudioFolders(): List<String> {
        val categories = mutableSetOf<String>()

        val projection = arrayOf(MediaStore.Audio.Media.DATA)
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"

        context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            null,
        )?.use { cursor ->
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            while (cursor.moveToNext()) {
                val filePath = cursor.getString(columnIndex)
                val category = filePath.substringBeforeLast("/")
                categories.add(category)
            }
        }
        return categories.toList()
    }

    override fun getAllAudioFiles(path: String): Flow<File> = flow {

        val folder = File(path)
        if (folder.exists() && folder.isDirectory) {
            folder.listFiles()?.let { files ->
                for (file in files) {
                    if (file.isFile && isAudioFile(file)) {
                        emit(file)
                    }
                }
            }
        }
    }.flowOn(Dispatchers.IO)

    private fun isAudioFile(file: File): Boolean {
        // Check if the file has an audio extension
        val audioExtensions = listOf(".mp3", ".wav", ".aac", ".ogg", ".m4a")
        for (extension in audioExtensions) {
            if (file.name.endsWith(extension, ignoreCase = true)) {
                return true
            }
        }
        return false
    }
}
