package com.example.musify.presentation.view_model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musify.data.room_db.dao.PlaylistAndSongDao
import com.example.musify.data.room_db.entity.Playlist
import com.example.musify.data.room_db.entity.PlaylistSongCrossRef
import com.example.musify.data.room_db.entity.PlaylistWithSongs
import com.example.musify.presentation.delegates.PlaylistUiStateDelegate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val playlistAndSongDao: PlaylistAndSongDao,
    private val playlistUiStateDelegate: PlaylistUiStateDelegate
) :
    ViewModel(), PlaylistUiStateDelegate by playlistUiStateDelegate {

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

    fun getAllSongsFromPlaylist(playlistId: Long, callback: (PlaylistWithSongs) -> Unit) {
        handleDatabaseResult(storageVariable = {
            callback(it)
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

