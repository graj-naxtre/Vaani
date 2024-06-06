package com.example.musify.presentation.ui.playlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musify.data.room_db.dao.PlaylistAndSongDao
import com.example.musify.data.room_db.entity.Playlist
import com.example.musify.data.room_db.entity.PlaylistSongCrossRef
import com.example.musify.domain.service.MusicController
import com.example.musify.presentation.delegates.PlaylistUiStateDelegate
import com.example.musify.presentation.viewmodels.AudioFileInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val playlistAndSongDao: PlaylistAndSongDao,
    private val playlistUiStateDelegate: PlaylistUiStateDelegate,
    private val musicController: MusicController
) :
    ViewModel(), PlaylistUiStateDelegate by playlistUiStateDelegate {
    var playlistUiState by mutableStateOf(PlaylistUiState())
        private set

    private fun playSong() {
        // set media items if user played song from new playlist
        if (playlistUiState.viewPlaylistId != playlistUiState.currentPlaylistId) {
            playlistUiState = playlistUiState.copy(
                currentPlaylistId = playlistUiState.viewPlaylistId,
                currentPlaylistSongs = playlistUiState.viewPlaylistSongs,
            )
            musicController.addMediaItems(playlistUiState.currentPlaylistSongs)
        }
        // play the selected song
        playlistUiState.apply {
            currentPlaylistSongs.indexOf(selectedSong).let { songIndex ->
                musicController.play(songIndex)
            }
        }
    }

    fun createPlaylist(playlistName: String, callback: (Long) -> Unit) {
        handleDatabaseResult(storageVariable = {
            callback(it)
        }) { playlistAndSongDao.createPlaylist(Playlist(playlistName = playlistName)) }
    }

    fun getAllPlaylist(callback: (List<Playlist>) -> Unit) {
        handleDatabaseResult(storageVariable = {
            callback(it)
        }) { playlistAndSongDao.getAllPlaylist() }
    }

    fun getAllSongsFromPlaylist(playlistId: Long) {
        handleDatabaseResult(storageVariable = { playlistWithSongs ->
            val audioFiles = playlistWithSongs.songs.map { it.toAudioFileInfo() }
            playlistUiState =
                playlistUiState.copy(
                    viewPlaylistSongs = audioFiles,
                    viewPlaylistId = playlistId
                )
        }) { playlistAndSongDao.getAllSongsFromPlaylist(playlistId = playlistId) }
    }

    fun addSongToPlaylist(playlistId: Long, audioFileInfo: AudioFileInfo) {
        viewModelScope.launch {
            val result = async {
                playlistAndSongDao.insertSong(audioFileInfo.toSongMapper())
            }
            val mediaId = result.await()
            handleDatabaseResult {
                playlistAndSongDao.addSongToPlaylist(
                    PlaylistSongCrossRef(
                        playlistId,
                        mediaId
                    )
                )
            }
        }
    }

    fun removeSongFromPlaylist(playlistId: Long, mediaId: Long) {
        handleDatabaseResult {
            playlistAndSongDao.removeSongFromPlaylist(
                PlaylistSongCrossRef(
                    playlistId,
                    mediaId
                )
            )
        }
    }

    fun deletePlaylist(playlistId: Long) {
        handleDatabaseResult { playlistAndSongDao.deletePlaylist(playlistId) }
    }

    private fun <T : Any> handleDatabaseResult(
        storageVariable: (T) -> Unit = {},
        databaseCall: suspend () -> T
    ) {
        viewModelScope.launch {
            startLoading()
            val response: PlaylistState<T> = launchAsync { databaseCall() }
            when (response) {
                is PlaylistState.SUCCESS -> {
                    storageVariable(response.data)
                    resetState()
                }

                is PlaylistState.ERROR -> {
                    errorOccurred(message = response.message)
                }
            }
        }
    }

    private suspend fun <T : Any> launchAsync(execute: suspend () -> T): PlaylistState<T> {
        return try {
            val data = execute()
            PlaylistState.SUCCESS(data = data)
        } catch (e: Exception) {
            PlaylistState.ERROR("message: ${e.message}")
        }
    }
}

sealed class PlaylistState<T : Any> {
    data class SUCCESS<T : Any>(val data: T) : PlaylistState<T>()
    data class ERROR<T : Any>(val message: String) : PlaylistState<T>()
}

