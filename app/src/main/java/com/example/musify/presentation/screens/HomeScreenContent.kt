package com.example.musify.presentation.screens

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musify.R
import com.example.musify.data.model.AppFolderWithSongs
import com.example.musify.presentation.composables.ActionButton
import com.example.musify.presentation.composables.HeaderV2
import com.example.musify.presentation.composables.MiniPlayer
import com.example.musify.presentation.composables.MusicPlayer
import com.example.musify.presentation.composables.SongItem
import com.example.musify.presentation.view_model.AudioFileInfo
import com.example.musify.presentation.view_model.HomeScreenViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
fun HomeScreenContent(
    data: List<AppFolderWithSongs>,
    viewModel: HomeScreenViewModel,
    onSearchClick: () -> Unit,
    onPlaylistClick: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = data.size)
    val scope = rememberCoroutineScope()
    var showMiniPlayer by remember { mutableStateOf(false) }
    var showMusicPlayer by remember { mutableStateOf(false) }
    val currentSongIndex = remember { mutableIntStateOf(0) }

    val currentProgress = remember {
        mutableLongStateOf(0L)
    }

    LaunchedEffect(key1 = currentSongIndex.intValue) {
        viewModel.myAudioPlayer.getCurrentPlaybackProgressFlow().collectLatest { currentPosition ->
            currentProgress.longValue = currentPosition
        }
    }

    Log.i("Seekbar", "${currentProgress}")

    Scaffold(
        topBar = {
            HeaderV2(onSearchClick = onSearchClick)
        },
        floatingActionButton = { ActionButton() },
        bottomBar = {
            if (showMiniPlayer) {
                MiniPlayer(
                    myAudioPlayer = viewModel.myAudioPlayer,
                    mediaPlayerStateHandler = viewModel.mediaPlayerStateHandler,
                    onClick = { showMusicPlayer = true; },
                    viewModel = viewModel,
                    currentProgress = currentProgress,
                    onPlaylistClick = onPlaylistClick
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        listOf(
                            colorResource(id = R.color.top_color),
                            colorResource(id = R.color.bottom_color)
                        )
                    )
                )
        ) {
            ScrollableTabRow(
                modifier = Modifier.fillMaxWidth(),
                selectedTabIndex = pagerState.currentPage,
                edgePadding = 16.dp,
                contentColor = Color.LightGray,
                backgroundColor = colorResource(id = R.color.top_color),
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        color = colorResource(id = R.color.orange),
                        modifier = Modifier
                            .pagerTabIndicatorOffset(pagerState, tabPositions)
                            .fillMaxWidth()
                    )
                }
            ) {
                data.forEachIndexed { index, appFolderWithSongs ->
                    Tab(selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        selectedContentColor = colorResource(id = R.color.orange),
                        content = {
                            Text(
                                text = "${appFolderWithSongs.songFolder}",
                                color = if (pagerState.currentPage == index) colorResource(id = R.color.orange) else Color.LightGray,
                                fontSize = 20.sp,
                                modifier = Modifier.padding(10.dp),
                            )
                        }
                    )
                }
            }
            HorizontalPager(state = pagerState, verticalAlignment = Alignment.Top) { index ->
                val songs = remember { data[index].songFiles }
                LazyColumn {
                    itemsIndexed(
                        songs,
                        key = { _: Int, item: AudioFileInfo -> item.fileName }) { index, audioFile ->
                        SongItem(
                            songIndex = index,
                            audioFileInfo = audioFile,
                            viewModel = viewModel,
                            showMiniPlayer = { showMiniPlayer = true },
                            songClick = { currentSongIndex.intValue = it },
                            optionClick = {}
                        )
                    }
                }
            }

            if (showMusicPlayer) {
                MusicPlayer(
                    currentSongIndex = currentSongIndex.intValue,
                    currentProgress = currentProgress,
                    totalDuration = viewModel.mediaPlayerStateHandler.songDuration.value,
                    showBottomSheet = { showMusicPlayer = it },
                    myAudioPlayer = viewModel.myAudioPlayer,
                    mediaPlayerStateHandler = viewModel.mediaPlayerStateHandler
                )
            }
        }
    }
}
