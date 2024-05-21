package com.example.musify.presentation.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.musify.R
import com.example.musify.data.room_db.entity.Playlist
import com.example.musify.presentation.composables.SongImage
import com.example.musify.presentation.delegates.PlaylistUiState
import com.example.musify.presentation.view_model.PlaylistViewModel

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
            IconButton(
                onClick = { onBackClick() },
                modifier = Modifier
                    .padding(10.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            ) {
                Image(imageVector = Icons.Filled.ArrowBack, contentDescription = "back icon")
            }
        },
        modifier = Modifier.background(
            Brush.linearGradient(
                listOf(
                    colorResource(id = R.color.top_color),
                    colorResource(id = R.color.bottom_color)
                )
            )
        )
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
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
                    Column {
                        TextField(value = newPlaylistName, onValueChange = { newPlaylistName = it })
                        Button(onClick = {
                            viewModel.createPlaylist(newPlaylistName) {}
                            isDialogOpen = false
                        }) {
                            Text(text = "Create")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CreatePlaylist(onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .background(color = Color.DarkGray, shape = RoundedCornerShape(5.dp))
            .padding(vertical = 10.dp, horizontal = 15.dp)
            .clickable {
                onClick()
            }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(Color.Gray)
                .border(width = 2.dp, color = Color.White)
                .padding(10.dp)
        ) {
            Image(imageVector = Icons.Filled.Add, contentDescription = "add icon")
        }
        Text(text = "Create Playlist", style = MaterialTheme.typography.h6)
    }
}

@Composable
fun PlaylistItem(playlistName: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.DarkGray, shape = RoundedCornerShape(5.dp))
            .padding(vertical = 10.dp, horizontal = 15.dp)
            .clickable { onClick() }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(Color.Gray)
                .border(width = 2.dp, color = Color.White)
        ) {
            SongImage(songImage = null, modifier = Modifier.size(20.dp))
        }
        Text(text = playlistName, style = MaterialTheme.typography.h6)
    }
}


//@Preview(showSystemUi = true)
//@Composable
//fun PlaylistPreview() {
//
//    PlaylistScreen(viewModel = , onPlaylistClick = {})
//}