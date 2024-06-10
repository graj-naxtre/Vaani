package com.example.musify.presentation.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.musify.R
import com.example.musify.domain.other.PlayerState
import com.example.musify.presentation.ui.song.SongEvent
import com.example.musify.presentation.viewmodels.MusicControllerUiState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MiniPlayer(
    state: MusicControllerUiState,
    onEvent: (SongEvent) -> Unit,
    onClick: () -> Unit,
    onPlaylistClick: () -> Unit
) {
    val context = LocalContext.current
    val model = remember(key1 = state.currentSong) {
        ImageRequest.Builder(context)
            .data(state.currentSong?.mediaImage)
            .placeholder(R.drawable.fallback_image)
            .fallback(R.drawable.fallback_image)
            .crossfade(true)
            .build()
    }

    val togglePlayPause: () -> Unit = {
        if (state.playerState == PlayerState.PLAYING) {
            onEvent(SongEvent.Pause)
        } else {
            onEvent(SongEvent.Resume)
        }
    }
    if (state.playerState != PlayerState.STOPPED) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(modifier = Modifier
                .height(80.dp)
                .fillMaxWidth()
                .clickable { onClick() }) {
                LinearProgressIndicator(
                    progress = { state.currentPosition.toFloat() / state.totalDuration },
                    modifier = Modifier.fillMaxWidth(),
                    color = colorResource(id = R.color.orange),
                    trackColor = Color.Transparent,
                )
                Row(
                    modifier = Modifier
                        .height(78.dp)
                        .fillMaxWidth()
                        .background(colorResource(id = R.color.top_color)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = model,
                        contentDescription = "Song cover",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.aspectRatio(1f / 1f)
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 10.dp)
                    ) {
                        Text(
                            text = "${state.currentSong?.fileName}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .basicMarquee(
                                    iterations = 1000,
                                    spacing = MarqueeSpacing(spacing = 100.dp)
                                )
                        )
                        Text(text = "${state.currentSong?.artistName}")
                    }
                    Row(modifier = Modifier.padding(20.dp)) {
                        IconButton(
                            onClick = { togglePlayPause() }, modifier = Modifier
                                .aspectRatio(1f / 1f)
                        ) {
                            Icon(
                                imageVector = if (state.playerState == PlayerState.PLAYING)
                                    ImageVector.vectorResource(R.drawable.pause_icon)
                                else ImageVector.vectorResource(R.drawable.play_icon),
                                contentDescription = "play or pause icon",
                                tint = colorResource(id = R.color.orange)
                            )
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                        IconButton(
                            onClick = { onPlaylistClick() }, modifier = Modifier
                                .aspectRatio(1f / 1f)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.playlist_icon),
                                contentDescription = "Playlist icon",
                                tint = colorResource(id = R.color.orange),
                            )
                        }
                    }
                }
            }
        }
    }
}