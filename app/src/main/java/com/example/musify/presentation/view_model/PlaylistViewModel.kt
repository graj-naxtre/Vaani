package com.example.musify.presentation.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musify.data.room_db.dao.PlaylistAndSongDao
import com.example.musify.data.room_db.entity.Playlist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(private val playlistAndSongDao: PlaylistAndSongDao) :
    ViewModel() {

    fun createPlaylist(playlistName: String) {
        viewModelScope.launch {

        }
    }

    fun addSongToPlaylist() {

    }


    fun removeSongFromPlaylist(mediaId: Long){

    }

    fun getAllSongsFromPlaylist(playlistId: Long) {
        viewModelScope.launch {

        }
    }

    fun deletePlaylist(playlistId: Long) {
        viewModelScope.launch {
           val response =  launchAsync { playlistAndSongDao.deletePlaylist(playlistId) }
            when(response){
                is PlaylistState.Success<*> -> {}
                is PlaylistState.Error -> {}
            }
        }
    }

    private suspend fun <T: Any> launchAsync(execute: suspend () -> T) : PlaylistState<T> {
            return runCatching {
                val result = execute()
                PlaylistState.SUCCESS(data = result)
            }.onFailure {
                PlaylistState.ERROR("${it.message}")
            }
    }
}

sealed class PlaylistState <T: Any>{
    data class SUCCESS<T: Any>(val data: T) : PlaylistState<T>()
    data class ERROR<T : Any>(val message: String) : PlaylistState<T>()
}

