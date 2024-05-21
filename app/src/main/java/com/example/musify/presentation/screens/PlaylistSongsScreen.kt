package com.example.musify.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.musify.R
import com.example.musify.data.room_db.entity.PlaylistWithSongs
import com.example.musify.data.room_db.entity.Song
import com.example.musify.presentation.delegates.PlaylistUiState
import com.example.musify.presentation.view_model.PlaylistViewModel

@Composable
fun PlaylistSongsScreen(viewModel: PlaylistViewModel, playlistId: String?, onBackClick: () -> Unit) {
    val state by viewModel.state.collectAsState()
    var playlistWithSongs by remember {
        mutableStateOf<PlaylistWithSongs?>(null)
    }

    LaunchedEffect(key1 = Unit) {
        if (!playlistId.isNullOrEmpty()) {
            viewModel.getAllSongsFromPlaylist(playlistId.toLong()){
                playlistWithSongs = it
            }
        }
    }

    Scaffold(
        topBar = {
            PlaylistSongsHeader(onClick = onBackClick)
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
            when (state) {
                PlaylistUiState.LOADING -> {
                    Text(text = "Loading...")
                }

                PlaylistUiState.IDLE -> {
                    if(playlistWithSongs != null){
                        LazyColumn {
                            itemsIndexed(
                                items = playlistWithSongs!!.songs,
                                key = { _: Int, item: Song -> item.mediaName }) { index, song ->
//                            SongItem(
//                                songIndex = index,
//                                audioFileInfo = audioFile,
//                                viewModel = viewModel,
//                                showMiniPlayer = { showMiniPlayer = true },
//                                songClick = { currentSongIndex.value = it },
//                                optionClick = {}
//                            )
                                Text(text = song.mediaName)
                            }
                        }
                    } else {
                        Text(text = "null value")
                    }
                }

                is PlaylistUiState.ERROR -> {
                    Text(text = (state as PlaylistUiState.ERROR).errorMessage)
                }
            }
        }
    }
}

@Composable
fun PlaylistSongsHeader(onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(10.dp)
    ) {
        IconButton(
            onClick = { onClick() },
            modifier = Modifier
                .weight(1f)
                .clip(CircleShape)
                .background(Color.Gray)
        ) {
            Image(imageVector = Icons.Filled.ArrowBack, contentDescription = "back icon")
        }
        Text(
            text = "Playlist Name",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.weight(8f),
            textAlign = TextAlign.Center
        )
        Box(modifier = Modifier.weight(1f))
    }
}

//@Preview(showSystemUi = true)
//@Composable
//fun PlaylistSongsPreview() {
//    PlaylistSongsScreen("playlistId")
//}