package com.example.musify.presentation.composables

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import com.example.musify.R
import com.example.musify.domain.player.MediaPlayerStateHandler
import com.example.musify.domain.player.MyAudioPlayer
import com.example.musify.domain.player.PlaybackState
import com.example.musify.presentation.utils.getCurrentPlaybackProgressFlow
import com.example.musify.presentation.view_model.HomeScreenViewModel
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun MiniPlayer(
    myAudioPlayer: MyAudioPlayer,
    mediaPlayerStateHandler: MediaPlayerStateHandler,
    onClick: () -> Unit,
    viewModel: HomeScreenViewModel,
    currentProgress: State<Long>,
    onPlaylistClick: () -> Unit
) {
    val isPlaying =
        mediaPlayerStateHandler.playerState.value == PlaybackState.PLAYING || mediaPlayerStateHandler.playerState.value == PlaybackState.READY

    val (songName, artistName, songDuration, songImage) = viewModel.mediaPlayerStateHandler.songState.value

    val togglePlayPause: () -> Unit = {
        if (isPlaying) {
            myAudioPlayer.pause()
            Log.d("playback state", "paused")
            mediaPlayerStateHandler.playerState.value = PlaybackState.PAUSED
        } else {
            myAudioPlayer.play()
            Log.d("playback state", "play")
            mediaPlayerStateHandler.playerState.value = PlaybackState.PLAYING
        }
    }

    var isSongPlaying by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying_: Boolean) {
                isSongPlaying = isPlaying_
            }
        }
        myAudioPlayer.exoPlayer.addListener(listener)
        onDispose {
            myAudioPlayer.exoPlayer.removeListener(listener)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable { onClick() }
    ) {
        Column {
            LinearProgressIndicator(
                progress = currentProgress.value.toFloat() / songDuration,
                color = colorResource(id = R.color.orange),
                trackColor = Color.Transparent,
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.top_color)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SongImage(
                    songImage = songImage,
                    modifier = Modifier
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 10.dp)
                ) {
                    Text(
                        text = songName,
                        modifier = Modifier
                            .fillMaxWidth()
                            .basicMarquee(
                                iterations = 1000,
                                spacing = MarqueeSpacing(spacing = 100.dp)
                            )
                    )
                    Text(text = "$artistName")
                }
                Row(modifier = Modifier.padding(20.dp)) {
                    IconButton(
                        onClick = { togglePlayPause() }, modifier = Modifier
                            .aspectRatio(1f / 1f)
                    ) {
                        Image(
                            imageVector = if (isPlaying) ImageVector.vectorResource(R.drawable.pause_icon) else ImageVector.vectorResource(
                                R.drawable.play_icon
                            ),
                            contentDescription = "play or pause icon",
                            contentScale = ContentScale.Fit,
                            colorFilter = ColorFilter.tint(colorResource(id = R.color.orange))
                        )
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    IconButton(
                        onClick = { onPlaylistClick() }, modifier = Modifier
                            .aspectRatio(1f / 1f)
                    ) {
                        Image(
                            imageVector = ImageVector.vectorResource(R.drawable.playlist_icon),
                            contentDescription = "Playlist icon",
                            contentScale = ContentScale.Fit,
                            colorFilter = ColorFilter.tint(colorResource(id = R.color.orange))
                        )
                    }
                }
            }
        }
    }
}

//    var currentValue by remember { mutableStateOf(0L) }

//    if (isSongPlaying) {
//        LaunchedEffect(Unit) {
//            while (true) {
//                currentValue = myAudioPlayer.exoPlayer.currentPosition
//                delay(1.seconds / 30)
//            }
//        }
//    }