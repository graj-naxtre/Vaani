package com.example.musify.presentation.view_model

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.musify.data.model.AppFolderWithSongs
import com.example.musify.data.model.FolderWithSongs
import com.example.musify.domain.player.MediaPlayerStateHandler
import com.example.musify.domain.player.MyAudioPlayer
import com.example.musify.domain.repository.MusicRepository
import com.example.musify.presentation.utils.toAudiFileInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
    val myAudioPlayer: MyAudioPlayer,
    val mediaPlayerStateHandler: MediaPlayerStateHandler,
) :
    ViewModel() {
    private var _categoryPaths: MutableState<List<String>> = mutableStateOf(emptyList())
//    var categoryPaths: State<List<String>> = _categoryPaths

    private var _tabs: MutableState<List<String>> = mutableStateOf(emptyList())
    var tabs: State<List<String>> = _tabs

    private var _audioFiles: MutableStateFlow<List<AudioFileInfo>> = MutableStateFlow(emptyList())
    var audioFiles: StateFlow<List<AudioFileInfo>> = _audioFiles.asStateFlow()

    private val _state = MutableStateFlow<MusifyUiState>(MusifyUiState.LOADING)
    val state: StateFlow<MusifyUiState> = _state.asStateFlow()

    init {
//        getAllAudioFolders()
        getAllMedia()
    }

    fun getAllAudioFolders() {
        _categoryPaths.value = emptyList()
        _tabs.value = emptyList()

        viewModelScope.launch {
            _categoryPaths.value = musicRepository.getAllAudioFolders()
            Log.d("files", "${_categoryPaths.value}")
            _categoryPaths.value.forEachIndexed { index, s ->
                _tabs.value = _tabs.value.toMutableList().apply {
                    add(s.split("/").last())
                }
            }
        }
    }

    fun getAllAudioFiles(index: Int) {
        _audioFiles.value = emptyList()

        if (_categoryPaths.value.size > 0) {
            viewModelScope.launch {
                try {
                    musicRepository.getAllAudioFiles(_categoryPaths.value[index]).map { file ->
                        val audioFiles = file.toAudiFileInfo()
                        _audioFiles.value += audioFiles
                        Log.d("audio Files", "${audioFiles.fileName}")
                    }.collect()
                } catch (e: Exception) {
                    Log.d("error", "$e")
                }
            }
        }
    }

    fun setMediaItem(audioFileInfo: AudioFileInfo) {
        val mediaItem = MediaItem.Builder()
            .setUri(audioFileInfo.filePath)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(audioFileInfo.fileName)
                    .setAlbumArtist(audioFileInfo.artistName)
                    .setDisplayTitle(audioFileInfo.fileName)
                    .setArtworkData(
                        audioFileInfo.mediaImage,
                        MediaMetadata.PICTURE_TYPE_FRONT_COVER
                    )
                    .build()
            ).build()

        myAudioPlayer.setMediaItem(mediaItem = mediaItem)
    }

    fun getAllMedia() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.emit(MusifyUiState.LOADING)
            runCatching {
                musicRepository.getAllMediaSongs()
                    .map { folderWithSongs: FolderWithSongs ->
                        folderWithSongs.toAppFolderWithSongs()
                    }
                    .also { data: List<AppFolderWithSongs> -> _state.emit(MusifyUiState.SUCCESS(data)) }
            }.onFailure {
                _state.emit(MusifyUiState.ERROR("error: ${it.message}"))
            }
        }
    }

    fun setMediaItemList(audioFiles: List<AudioFileInfo>) {
        audioFiles.map { audioFileInfo ->
            MediaItem.Builder()
                .setUri(audioFileInfo.filePath)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(audioFileInfo.fileName)
                        .setAlbumArtist(audioFileInfo.artistName)
                        .setDisplayTitle(audioFileInfo.fileName)
                        .setDisplayTitle(audioFileInfo.fileName)
                        .setArtworkData(
                            audioFileInfo.mediaImage,
                            MediaMetadata.PICTURE_TYPE_FRONT_COVER
                        )
                        .build()
                ).build()
        }.also {
            myAudioPlayer.setMediaItemList(it)
        }
    }
}

sealed class MusifyUiState {
    data object LOADING : MusifyUiState()
    data class ERROR(val message: String) : MusifyUiState()
    data class SUCCESS(val data: List<AppFolderWithSongs>) : MusifyUiState()
}