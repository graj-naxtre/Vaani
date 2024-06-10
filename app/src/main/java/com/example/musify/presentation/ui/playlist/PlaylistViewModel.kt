package com.example.musify.presentation.ui.playlist

import android.util.Log
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
    var playlistUiState by mutableStateOf(PlaylistState())
        private set

    fun onEvent(event: PlaylistEvent) {
        when (event) {
            PlaylistEvent.OnPlay -> playSong()
            is PlaylistEvent.OnSongSelected -> playlistUiState =
                playlistUiState.copy(selectedSong = event.songSelected)

            is PlaylistEvent.OnRemoveFromPlaylist -> {
                if(event.playlistId != null && event.filePath != null){
                    getMediaId(filePath = event.filePath, callback = {mediaId ->
                        removeSongFromPlaylist(playlistId = event.playlistId, mediaId = mediaId)
                    })
                }
            }

            is PlaylistEvent.OnDeletePlaylist -> event.playlistId?.let {
                deletePlaylist(it)
            }
        }
    }

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
                    viewPlaylistName = playlistWithSongs.playlist.playlistName,
                    viewPlaylistId = playlistId,
                )
        }) { playlistAndSongDao.getAllSongsFromPlaylist(playlistId = playlistId) }
    }

    fun addSongToPlaylist(playlistId: Long, audioFileInfo: AudioFileInfo) {
        viewModelScope.launch {
            val result = async {
                playlistAndSongDao.insertSong(audioFileInfo.toSongMapper())
            }
            val mediaId = result.await()
            handleDatabaseResult(databaseCall = {
                playlistAndSongDao.addSongToPlaylist(
                    PlaylistSongCrossRef(
                        playlistId,
                        mediaId
                    )
                )
            })
        }
    }

    private fun getMediaId(filePath: String, callback: (Long) -> Unit) {
        handleDatabaseResult(storageVariable = {
            callback(it)
        }, databaseCall = { playlistAndSongDao.getMediaId(filePath) })
    }

    private fun removeSongFromPlaylist(playlistId: Long, mediaId: Long) {
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
            val response: DatabaseCallState<T> = launchAsync { databaseCall() }
            when (response) {
                is DatabaseCallState.SUCCESS -> {
                    storageVariable(response.data)
                    resetState()
                }

                is DatabaseCallState.ERROR -> {
                    errorOccurred(message = response.message)
                }
            }
        }
    }
}

