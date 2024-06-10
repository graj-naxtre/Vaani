package com.example.musify.presentation.ui.song

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.musify.R
import com.example.musify.domain.other.PlayerState
import com.example.musify.domain.utils.formatMinSec
import com.example.musify.presentation.viewmodels.MusicControllerUiState
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.placeholder
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MusicPlayer(
    state: MusicControllerUiState,
    onEvent: (SongEvent) -> Unit,
    showBottomSheet: (Boolean) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var position by remember { mutableLongStateOf(state.currentPosition) }

    val model = remember(key1 = state.currentSong) {
        ImageRequest.Builder(context)
            .data(state.currentSong?.mediaImage)
            .placeholder(R.drawable.fallback_image)
            .fallback(R.drawable.fallback_image)
            .crossfade(true)
            .build()
    }
    var isImageLoadingPlaceholderVisible by remember { mutableStateOf(true) }

    val togglePlayPause: () -> Unit = {
        if (state.playerState == PlayerState.PLAYING) {
            onEvent(SongEvent.Pause)
        } else {
            onEvent(SongEvent.Resume)
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
                    modifier = Modifier
                        .aspectRatio(1f / 1f)
                        .placeholder(
                            visible = isImageLoadingPlaceholderVisible,
                            highlight = PlaceholderHighlight.shimmer(),
                            color = Color.White
                        ),
                    model = model,
                    contentDescription = "Song cover",
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.Center,
                    onSuccess = { isImageLoadingPlaceholderVisible = false },
                    onError = { isImageLoadingPlaceholderVisible = false },
                    onLoading = { isImageLoadingPlaceholderVisible = true }
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
                            value = state.currentPosition.toFloat(),
                            onValueChange = { newPosition: Float ->
                                position = newPosition.toLong()
                            },
                            valueRange = 0f..state.totalDuration.toFloat(),
                            onValueChangeFinished = { onEvent(SongEvent.SeekTo(position)) },
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
                            Text(text = state.currentPosition.formatMinSec())
                            Text(text = state.totalDuration.formatMinSec())
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    IconButton(
                        onClick = { onEvent(SongEvent.SkipToPreviousSong) },
                        Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.previous_song),
                            contentDescription = "Previous Songs Icon",
                            tint = colorResource(id = R.color.white)
                        )
                    }
                    IconButton(onClick = { togglePlayPause() }, Modifier.weight(1f)) {
                        Icon(
                            imageVector = if (state.playerState == PlayerState.PLAYING)
                                ImageVector.vectorResource(R.drawable.pause_icon)
                            else ImageVector.vectorResource(R.drawable.play_icon),
                            contentDescription = "Play Pause Icon",
                            tint = colorResource(id = R.color.white)
                        )
                    }
                    IconButton(
                        onClick = { onEvent(SongEvent.SkipToNextSong) },
                        Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.next_song),
                            contentDescription = "Next Song Icon",
                            tint = colorResource(id = R.color.white)
                        )
                    }
                }
            }
        }
    }
}