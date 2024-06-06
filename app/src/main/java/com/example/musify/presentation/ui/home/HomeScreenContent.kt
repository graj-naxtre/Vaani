package com.example.musify.presentation.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.musify.presentation.ui.home.components.HeaderV2
import com.example.musify.presentation.ui.home.components.OptionsBottomSheet
import com.example.musify.presentation.ui.home.components.SongItem
import com.example.musify.presentation.viewmodels.AudioFileInfo
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    onEvent: (HomeEvent) -> Unit,
    onSearchClick: () -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = uiState.folderWithSongs.size)
    val scope = rememberCoroutineScope()
    var showOptionsSheet by remember {
        mutableStateOf(false)
    }

//    Scaffold(
//        floatingActionButton = { ActionButton() },
//    ) {}
    Column (modifier = Modifier.fillMaxSize()){
        HeaderV2(onSearchClick = onSearchClick)
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
                uiState.folderWithSongs.forEachIndexed { index, appFolderWithSongs ->
                    Tab(selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                                onEvent(HomeEvent.onFolderIndexChange(index))
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
                val songs = remember { uiState.folderWithSongs[index].songFiles }
                LazyColumn {
                    itemsIndexed(
                        songs,
                        key = { _: Int, item: AudioFileInfo -> item.fileName }) { _, audioFile ->
                        SongItem(
                            audioFileInfo = audioFile,
                            songClick = {
                                onEvent(HomeEvent.OnSongSelected(audioFile));
                                onEvent(HomeEvent.PlaySong)
                            },
                            optionClick = {showOptionsSheet = it}
                        )
                    }
                }
            }

            if (showOptionsSheet) {
                OptionsBottomSheet {
                    showOptionsSheet = it
                }
            }
        }
    }
}
