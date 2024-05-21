package com.example.musify.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.musify.presentation.view_model.HomeScreenViewModel
import com.example.musify.presentation.view_model.MusifyUiState

@Composable
fun HomeScreenV2(
    viewModel: HomeScreenViewModel,
    onSearchClick: () -> Unit,
    onPlaylistClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    when (val response = state) {
        MusifyUiState.LOADING -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Loading...")
            }
        }

        is MusifyUiState.ERROR -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = response.message)
            }
        }

        is MusifyUiState.SUCCESS -> {
            HomeScreenContent(response.data, viewModel, onSearchClick, onPlaylistClick)
        }
    }
}
