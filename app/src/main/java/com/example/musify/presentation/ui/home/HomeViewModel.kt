package com.example.musify.presentation.ui.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musify.data.model.AppFolderWithSongs
import com.example.musify.data.model.FolderWithSongs
import com.example.musify.data.room_db.dao.PlaylistAndSongDao
import com.example.musify.data.room_db.entity.PlaylistSongCrossRef
import com.example.musify.domain.repository.MusicRepository
import com.example.musify.domain.service.MusicController
import com.example.musify.presentation.viewmodels.AudioFileInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
    private val playlistAndSongDao: PlaylistAndSongDao,
    private val musicController: MusicController
) : ViewModel() {
    var homeUiState by mutableStateOf(HomeUiState())
        private set

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.FetchSongs -> fetchFolderWithSongs()
            HomeEvent.PauseSong -> pauseSong()
            HomeEvent.PlaySong -> playSong()
            HomeEvent.ResumeSong -> resumeSong()
            is HomeEvent.OnSongSelected -> homeUiState =
                homeUiState.copy(selectedSong = event.selectedSong)

            is HomeEvent.onFolderIndexChange -> homeUiState =
                homeUiState.copy(currentFolderIndex = event.index)
        }
    }

    private fun fetchFolderWithSongs() {
        homeUiState = homeUiState.copy(loading = true)
        viewModelScope.launch {
            runCatching {
                musicRepository.getAllMediaSongs()
                    .map { folderWithSongs: FolderWithSongs ->
                        folderWithSongs.toAppFolderWithSongs()
                    }
                    .also { data: List<AppFolderWithSongs> ->
                        homeUiState = homeUiState.copy(folderWithSongs = data, loading = false)
                        Log.d("HomeViewModel", "songs loaded")
                    }
            }.onSuccess {
                initializeMediaItems()
            }.onFailure {
                homeUiState = homeUiState.copy(errorMessage = it.message, loading = false)
            }
        }
    }

    private fun playSong() {
        homeUiState.apply {
            folderWithSongs[currentFolderIndex].songFiles.indexOf(selectedSong)
                .let { songIndex ->
                    musicController.play(songIndex)
                }
        }
    }

    private fun initializeMediaItems() {
        homeUiState.apply {
            musicController.addMediaItems(folderWithSongs[currentFolderIndex].songFiles)
        }
    }

    private fun pauseSong() = musicController.pause()

    private fun resumeSong() = musicController.resume()

    fun addSongToPlaylist(playlistId: Long, audioFileInfo: AudioFileInfo) {
        viewModelScope.launch {
            val result = async {
                playlistAndSongDao.insertSong(audioFileInfo.toSongMapper())
            }
            val mediaId = result.await()

            playlistAndSongDao.addSongToPlaylist(
                PlaylistSongCrossRef(
                    playlistId,
                    mediaId
                )
            )
        }
    }
}