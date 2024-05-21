package com.example.musify.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.musify.presentation.screens.HomeScreenV2
import com.example.musify.presentation.screens.PlaylistScreen
import com.example.musify.presentation.screens.PlaylistSongsScreen
import com.example.musify.presentation.screens.SearchScreen
import com.example.musify.presentation.view_model.HomeScreenViewModel
import com.example.musify.presentation.view_model.PlaylistViewModel

@Composable
fun AppNavigation(navController: NavHostController) {
    val homeViewModel: HomeScreenViewModel = viewModel<HomeScreenViewModel>()
    val playlistViewModel: PlaylistViewModel = viewModel<PlaylistViewModel>()

    NavHost(navController = navController, startDestination = "home_screen") {
        composable(route = "home_screen") {
            HomeScreenV2(
                viewModel = homeViewModel,
                onSearchClick = { navController.navigate("search_screen") },
                onPlaylistClick = {
                    navController.navigate("playlist_screen") {
                        popUpTo("home_screen") {
                            inclusive = false
                        }
                    }
                })
        }

        composable(route = "search_screen") {
            SearchScreen()
        }

        composable(
            route = "playlist_screen",
        ) {
            PlaylistScreen(
                viewModel = playlistViewModel,
                onBackClick = { navController.popBackStack() },
                onPlaylistClick = { playlistId ->
                    navController.navigate("playlist_songs_screen/${playlistId}")
                }
            )
        }

        composable(route = "playlist_songs_screen/{playlistId}") { navBackStackEntry ->
            val playlistId = navBackStackEntry.arguments?.getString("playlistId")
            PlaylistSongsScreen(
                viewModel = playlistViewModel,
                playlistId = playlistId,
                onBackClick = {
                    navController.popBackStack()
                })
        }
    }
}


