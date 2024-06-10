package com.example.musify.presentation.ui.home

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.musify.R
import com.example.musify.presentation.MainActivity

@Composable
fun HomeScreenV2(
    onEvent: (HomeEvent) -> Unit,
    uiState: HomeUiState,
    onSearchClick: () -> Unit,
    onAddToPlaylistClick: () -> Unit
) {
    val preloaderLottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.loading
        )
    )

    LaunchedEffect(key1 = Unit) {
        if (!MainActivity.isInitialized) {
            Log.d("AppNavigation", "is initialized")
            onEvent(HomeEvent.FetchSongs)
            MainActivity.isInitialized = true
        }
    }

    with(uiState) {
        when {
            loading == true -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    LottieAnimation(
                        composition = preloaderLottieComposition,
                        iterations = LottieConstants.IterateForever,
                        contentScale = ContentScale.Fit
                    )
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
                    onAddToPlaylistClick = onAddToPlaylistClick
                )
            }
        }
    }
}
