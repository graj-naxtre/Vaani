package com.example.musify.presentation.ui.playlist

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.musify.data.room_db.entity.Playlist
import com.example.musify.presentation.delegates.PlaylistUiState
import com.example.musify.presentation.ui.home.HomeViewModel
import com.example.musify.presentation.ui.playlist.component.PlaylistHeader
import com.example.musify.presentation.ui.playlist.component.PlaylistItem
import com.example.musify.presentation.viewmodels.AudioFileInfo
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddToPlaylist(viewModel: HomeViewModel,onBackClick: () -> Unit) {
    val state by viewModel.state.collectAsState()
    var listOfPlaylist by remember {
        mutableStateOf<List<Playlist>>(emptyList())
    }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        viewModel.getAllPlaylist {
            listOfPlaylist = it
        }
    }
    Scaffold(
        topBar = {
            PlaylistHeader(title = "Add To Playlist", onBackClick = onBackClick)
        },
    ) { paddingValues ->
        when(state){
            is PlaylistUiState.ERROR -> {
                Toast.makeText(context, (state as PlaylistUiState.ERROR).errorMessage, Toast.LENGTH_LONG).show()
            }
             else -> Unit
        }
        Column(modifier = Modifier.padding(paddingValues)) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                stickyHeader {
                    Text(text = "Your Playlists")
                }
                items(items = listOfPlaylist) { playlist ->
                    PlaylistItem(
                        playlistName = playlist.playlistName,
                        onClick = {
                            scope.launch {
                                onBackClick()
                                viewModel.addSongToPlaylist(playlistId = playlist.playlistId)
                            }
                        })
                }
            }
        }
    }
}