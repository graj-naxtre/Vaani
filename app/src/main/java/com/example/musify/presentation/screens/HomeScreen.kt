package com.example.musify.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musify.R
import com.example.musify.presentation.view_model.AudioFileInfo
import com.example.musify.presentation.composables.ActionButton
import com.example.musify.presentation.composables.MiniPlayer
import com.example.musify.presentation.composables.MusicPlayer
import com.example.musify.presentation.composables.OptionsBottomSheet
import com.example.musify.presentation.composables.SongImage
import com.example.musify.presentation.composables.SongItem
import com.example.musify.presentation.view_model.HomeScreenViewModel

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel) {
    val folderIndex = remember { mutableStateOf(0) }
    val audioFiles = viewModel.audioFiles.collectAsState(initial = emptyList())

    var showMiniPlayer by remember { mutableStateOf(false) }

    val currentSongIndex = remember { mutableStateOf(0) }

    var showMusicPlayer by remember { mutableStateOf(false) }
    var showOptions by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Header(
                viewModel = viewModel,
                folderIndex = { value -> folderIndex.value = value })
        },
        floatingActionButton = { ActionButton() },
        bottomBar = {
            if (showMiniPlayer) {
//                MiniPlayer(
//                    myAudioPlayer = viewModel.myAudioPlayer,
//                    mediaPlayerStateHandler = viewModel.mediaPlayerStateHandler,
//                    onClick = { showMusicPlayer = true; },
//                    viewModel = viewModel,
//                    currentProgress =
//                    )
            }
        }
    ) { paddingValues ->
        LaunchedEffect(key1 = folderIndex.value) {
            viewModel.getAllAudioFiles(folderIndex.value)
        }
        Box(
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
        ) {
            LazyColumn {
//                viewModel.setMediaItemList(audioFiles.value)
                itemsIndexed(audioFiles.value, key = {index: Int, item: AudioFileInfo ->  item.fileName}){ index, audioFile ->
                    SongItem(
                        songIndex = index,
                        audioFileInfo = audioFile,
                        viewModel = viewModel,
                        showMiniPlayer = { showMiniPlayer = true },
                        songClick = { currentSongIndex.value = it},
                        optionClick = {showOptions = it}
                    )
                }
            }

            if (showMusicPlayer) {
//                MusicPlayer(
//                    currentSongIndex = currentSongIndex.value,
//                    currentPlaybackProgressFlow = viewModel.myAudioPlayer.getCurrentPlaybackProgressFlow(),
//                    totalDuration = viewModel.mediaPlayerStateHandler.songDuration.value,
//                    showBottomSheet = { showMusicPlayer = it },
//                    myAudioPlayer = viewModel.myAudioPlayer,
//                    mediaPlayerStateHandler = viewModel.mediaPlayerStateHandler
//                )
            }

            if(showOptions){
                OptionsBottomSheet(showOptions = {showOptions = it})
            }
        }
    }
}

@Composable
fun Header(viewModel: HomeScreenViewModel, folderIndex: (Int) -> Unit) {
    var isSearchEnabled by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    var selectedTabIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.top_color))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 20.dp),
        ) {
            Row {
                if (!isSearchEnabled) {
                    Text(text = "Musify", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                } else {
                    BasicTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        maxLines = 1
                    ) {
                        Text(text = "Search for a Song")
                    }
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { isSearchEnabled = !isSearchEnabled }) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search icon",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
        ScrollableTabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = selectedTabIndex,
            edgePadding = 16.dp,
            contentColor = Color.LightGray,
            containerColor = colorResource(id = R.color.top_color),
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    color = colorResource(id = R.color.orange),
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .fillMaxWidth()
                )
            }
        ) {
            viewModel.tabs.value.forEachIndexed { index, tab ->
                Tab(selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index; folderIndex(selectedTabIndex) },
                    selectedContentColor = colorResource(id = R.color.orange),
                    content = {
                        Text(
                            text = tab,
                            color = if (selectedTabIndex == index) colorResource(id = R.color.orange) else Color.LightGray,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(10.dp),
                        )
                    }
                )
            }
        }
    }
}