package com.example.musify.presentation.ui.playlist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.musify.R
import com.example.musify.data.room_db.entity.Playlist
import com.example.musify.presentation.delegates.PlaylistUiState
import com.example.musify.presentation.ui.playlist.component.CreatePlaylist
import com.example.musify.presentation.ui.playlist.component.PlaylistHeader
import com.example.musify.presentation.ui.playlist.component.PlaylistItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaylistScreen(
    viewModel: PlaylistViewModel,
    onBackClick: () -> Unit,
    onPlaylistClick: (Long) -> Unit
) {
    val state by viewModel.state.collectAsState()
    var isDialogOpen by remember {
        mutableStateOf(false)
    }
    var newPlaylistName by remember {
        mutableStateOf("")
    }
    var listOfPlaylist by remember {
        mutableStateOf<List<Playlist>>(emptyList())
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.getAllPlaylist {
            listOfPlaylist = it
        }
    }

    Scaffold(
        topBar = {
            PlaylistHeader(title = "Your Playlists", onBackClick = onBackClick)
        },
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
            CreatePlaylist(onClick = { isDialogOpen = true })
            when (state) {
                PlaylistUiState.LOADING -> {
                    Text(text = "Loading...")
                }

                is PlaylistUiState.ERROR -> {
                    Text(text = (state as PlaylistUiState.ERROR).errorMessage)
                }

                PlaylistUiState.IDLE -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        stickyHeader {
                            Text(text = "Your Playlists")
                        }
                        items(items = listOfPlaylist) { playlist ->
                            PlaylistItem(
                                playlistName = playlist.playlistName,
                                onClick = { onPlaylistClick(playlist.playlistId) })
                        }
                    }
                }
            }
            if (isDialogOpen) {
                Dialog(onDismissRequest = { isDialogOpen = false }) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        TextField(
                            value = newPlaylistName,
                            onValueChange = { newPlaylistName = it },
                            placeholder = {
                                Text(
                                    text = "Playlist_name",
                                    color = colorResource(id = R.color.orange),
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                unfocusedIndicatorColor = colorResource(id = R.color.orange),
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = colorResource(id = R.color.orange),
                                focusedContainerColor = Color.Transparent
                            ),
                            singleLine = true
                        )
                        Button(
                            onClick = {
                                viewModel.createPlaylist(newPlaylistName) {}
                                isDialogOpen = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(id = R.color.orange),
                                contentColor = Color.DarkGray
                            ),
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(text = "Create")
                        }
                    }
                }
            }
        }
    }
}
