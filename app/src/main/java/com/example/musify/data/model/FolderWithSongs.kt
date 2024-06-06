package com.example.musify.data.model


import com.example.musify.presentation.viewmodels.AudioFileInfo
import com.example.musify.presentation.utils.toAudiFileInfo
import java.io.File
import javax.annotation.concurrent.Immutable

data class FolderWithSongs(
    var songFolder: String? = null,
    var songFiles: List<File> = emptyList()
) {
    fun toAppFolderWithSongs(): AppFolderWithSongs {
        val listOfAudioFileInfo =  this.songFiles.map { songFile ->
            songFile.toAudiFileInfo()
        }
        return AppFolderWithSongs(
            songFolder = this.songFolder,
            songFiles = listOfAudioFileInfo
        )
    }
}

@Immutable
data class AppFolderWithSongs(
    var songFolder: String?,
    var songFiles: List<AudioFileInfo>
)
