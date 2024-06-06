package com.example.musify.presentation.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.musify.presentation.ui.home.HomeScreenV2
import com.example.musify.presentation.ui.home.HomeViewModel
import com.example.musify.presentation.ui.playlist.PlaylistScreen
import com.example.musify.presentation.ui.playlist.PlaylistSongsScreen
import com.example.musify.presentation.ui.playlist.PlaylistViewModel
import com.example.musify.presentation.ui.search.SearchScreen
import com.example.musify.presentation.ui.search.SearchViewModel

@Composable
fun AppNavigation(navController: NavHostController) {
    val homeViewModel: HomeViewModel = viewModel<HomeViewModel>()
    val playlistViewModel: PlaylistViewModel = viewModel<PlaylistViewModel>()
    val searchViewModel: SearchViewModel = viewModel<SearchViewModel>()

    NavHost(navController = navController, startDestination = Destination.Home.route) {
        composable(
            route = Destination.Home.route,
            exitTransition = {
                when (initialState.destination.route) {
                    Destination.Search.route -> {
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(700)
                        )
                    }

                    Destination.Playlist.route -> {
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Up,
                            tween(700)
                        )
                    }

                    else -> ExitTransition.None
                }

            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    Destination.Search.route -> {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(700)
                        )
                    }

                    Destination.Playlist.route -> {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Down,
                            tween(700)
                        )
                    }

                    else -> EnterTransition.None
                }
            }) {
            HomeScreenV2(
                onEvent = homeViewModel::onEvent,
                uiState = homeViewModel.homeUiState,
                onSearchClick = { navController.navigate(Destination.Search.route) },
                onPlaylistClick = {
                    navController.navigate(Destination.Playlist.route) {
                        popUpTo(Destination.Home.route) {
                            inclusive = false
                        }
                    }
                })
        }

        composable(
            route = Destination.Search.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            }) {
            SearchScreen(
                viewModel = searchViewModel,
                onBackClick = { navController.popBackStack() },
            )
        }

        composable(
            route = Destination.Playlist.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Up,
                    tween(700)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
                    tween(700)
                )
            }
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


