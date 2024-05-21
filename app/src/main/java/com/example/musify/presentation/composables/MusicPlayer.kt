package com.example.musify.presentation.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.musify.R
import com.example.musify.domain.utils.formatMinSec
import com.example.musify.domain.player.MediaPlayerStateHandler
import com.example.musify.domain.player.MyAudioPlayer
import com.example.musify.domain.player.PlaybackState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MusicPlayer(
    currentSongIndex: Int,
    currentProgress: State<Long>,
    totalDuration: Long,
    showBottomSheet: (Boolean) -> Unit,
    myAudioPlayer: MyAudioPlayer,
    mediaPlayerStateHandler: MediaPlayerStateHandler
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var position by remember { mutableLongStateOf(currentProgress.value) }

    val model = remember {
        ImageRequest.Builder(context)
            .data(myAudioPlayer.exoPlayer.mediaMetadata.artworkData)
            .crossfade(true)
            .build()
    }

    val isPlaying =
        mediaPlayerStateHandler.playerState.value == PlaybackState.PLAYING || mediaPlayerStateHandler.playerState.value == PlaybackState.READY
    val togglePlayPause: () -> Unit = {
        if (isPlaying) {
            myAudioPlayer.pause()
            mediaPlayerStateHandler.playerState.value = PlaybackState.PAUSED
        } else {
            myAudioPlayer.play()
            mediaPlayerStateHandler.playerState.value = PlaybackState.PLAYING
        }
    }

    ModalBottomSheet(
        onDismissRequest = { showBottomSheet(false) },
        sheetState = sheetState,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        listOf(
                            colorResource(id = R.color.top_color),
                            colorResource(id = R.color.bottom_color)
                        )
                    )
                ), verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet(false)
                        }
                    }
                }) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowRight,
                        contentDescription = "Icon",
                        modifier = Modifier.rotate(90f),
                        tint = Color.White
                    )
                }

                Text(text = "Musify", fontWeight = FontWeight.Bold)

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.three_vertical_dots),
                        contentDescription = "More icon",
                        tint = Color.White,
                        modifier = Modifier.scale(0.8f)
                    )
                }
            }

            // Poster Image
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    model = model,
                    fallback = painterResource(R.drawable.fallback_image),
                    placeholder = painterResource(R.drawable.fallback_image),
                    contentDescription = "Song cover",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.aspectRatio(1f / 1f),
                    alignment = Alignment.Center
                )
            }

            // Media Controllers
            Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 40.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(
                        modifier = Modifier
                            .weight(6f)
                            .padding(end = 15.dp)
                    ) {
                        Text(
                            text = myAudioPlayer.exoPlayer.mediaMetadata.displayTitle.toString(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .basicMarquee(
                                    iterations = 1000,
                                    spacing = MarqueeSpacing(spacing = 100.dp)
                                )
                        )
                        Text(text = myAudioPlayer.exoPlayer.mediaMetadata.albumArtist.toString())
                    }
                    IconButton(onClick = { /*TODO*/ }, modifier = Modifier.weight(1f)) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.favourite_unfilled),
                            contentDescription = "Favourite Icon",
                            tint = colorResource(id = R.color.white)
                        )
                    }
                    IconButton(onClick = { /*TODO*/ }, modifier = Modifier.weight(1f)) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.shuffle_inner),
                            contentDescription = "Shuffle Icon",
                            tint = colorResource(id = R.color.white)
                        )
                    }
                }

                Row(modifier = Modifier.padding(vertical = 30.dp)) {
                    Column {
                        Slider(
                            value = currentProgress.value.toFloat(),
                            onValueChange = { newPosition: Float -> position = newPosition.toLong()},
                            valueRange = 0f..totalDuration.toFloat(),
                            onValueChangeFinished = { myAudioPlayer.setNewPlaybackPosition(position) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = SliderDefaults.colors(
                                thumbColor = colorResource(id = R.color.orange),
                                activeTrackColor = colorResource(id = R.color.orange)
                            ),
                            enabled = true
                        )
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = currentProgress.value.formatMinSec())
                            Text(text = totalDuration.formatMinSec())
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    IconButton(
                        onClick = { myAudioPlayer.playPrevSong(currentSongIndex - 1) },
                        Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.previous_song),
                            contentDescription = "Shuffle Icon",
                            tint = colorResource(id = R.color.white)
                        )
                    }
                    IconButton(onClick = { togglePlayPause() }, Modifier.weight(1f)) {
                        Icon(
                            imageVector = if (isPlaying) ImageVector.vectorResource(R.drawable.pause_icon) else ImageVector.vectorResource(
                                R.drawable.play_icon
                            ),
                            contentDescription = "Shuffle Icon",
                            tint = colorResource(id = R.color.white)
                        )
                    }
                    IconButton(
                        onClick = { myAudioPlayer.playNextSong(currentSongIndex + 1) },
                        Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.next_song),
                            contentDescription = "Shuffle Icon",
                            tint = colorResource(id = R.color.white)
                        )
                    }
                }
            }
        }
    }
}