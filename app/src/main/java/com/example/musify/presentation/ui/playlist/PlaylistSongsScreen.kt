package com.example.musify.presentation.ui.playlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.musify.R
import com.example.musify.presentation.delegates.PlaylistUiState
import com.example.musify.presentation.ui.home.components.SongItem
import com.example.musify.presentation.ui.playlist.component.PlaylistHeader
import com.example.musify.presentation.ui.playlist.component.PlaylistOptions
import com.example.musify.presentation.viewmodels.AudioFileInfo
import kotlinx.coroutines.launch

@Composable
fun PlaylistSongsScreen(
    viewModel: PlaylistViewModel,
    onEvent: (PlaylistEvent) -> Unit,
    playlistId: String?,
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val playUiState = viewModel.playlistUiState
    var showOptionsSheet by remember {
        mutableStateOf(Pair<AudioFileInfo?, Boolean>(first = null, second = false))
    }
    var refresh by rememberSaveable {mutableStateOf(false)}
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = refresh) {
        if (!playlistId.isNullOrEmpty()) {
            viewModel.getAllSongsFromPlaylist(playlistId.toLong())
        }
        refresh = false
    }

    Scaffold(
        topBar = {
            PlaylistHeader(title = playUiState.viewPlaylistName, onBackClick = onBackClick)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        listOf(
                            colorResource(id = R.color.top_color),
                            colorResource(id = R.color.bottom_color)
                        )
                    )
                )
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (state) {
                PlaylistUiState.LOADING -> {
                    Text(text = "Loading...")
                }

                PlaylistUiState.IDLE -> {
                    if (playUiState.viewPlaylistSongs.isNotEmpty()) {
                        LazyColumn {
                            items(
                                items = playUiState.viewPlaylistSongs,
                                key = { item: AudioFileInfo -> item.fileName }) { song: AudioFileInfo ->
                                SongItem(
                                    audioFileInfo = song,
                                    songClick = {
                                        onEvent(PlaylistEvent.OnSongSelected(song));
                                        onEvent(PlaylistEvent.OnPlay)
                                    },
                                    optionClick = { value ->
                                        showOptionsSheet =
                                            showOptionsSheet.copy(first = song, second = value)
                                    }
                                )
                            }
                        }
                    } else {
                        Text(text = "Playlist empty hai")
                    }
                }

                is PlaylistUiState.ERROR -> {
                    Text(text = (state as PlaylistUiState.ERROR).errorMessage)
                }
            }
        }
        if (showOptionsSheet.second) {
            PlaylistOptions(
                showOptions = { showOptionsSheet = showOptionsSheet.copy(second = it) },
                onRemoveFromPlaylist = {
                    scope.launch {
                        onEvent(
                            PlaylistEvent.OnRemoveFromPlaylist(
                                playlistId = playlistId?.toLong(),
                                filePath = showOptionsSheet.first?.filePath
                            )
                        )
                        showOptionsSheet = showOptionsSheet.copy(second = false)
                        refresh = true
                    }
                },
                onAddToFav = {}
            )
        }
    }
}