package com.example.musify.presentation.ui.home

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun HomeScreenV2(
    onEvent: (HomeEvent) -> Unit,
    uiState: HomeUiState,
    onSearchClick: () -> Unit,
    onPlaylistClick: () -> Unit
) {
    val isInitialized = rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit) {
        if (!isInitialized.value) {
            Log.d("AppNavigation", "is initialized")
            onEvent(HomeEvent.FetchSongs)
            isInitialized.value = true
        }
    }

    with(uiState) {
        when {
            loading == true -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Loading...")
                }
            }

            errorMessage != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = errorMessage)
                }
            }

            loading == false && errorMessage == null -> {
                HomeScreenContent(
                    uiState = uiState,
                    onEvent = onEvent,
                    onSearchClick = onSearchClick,
                )
            }
        }
    }
}
