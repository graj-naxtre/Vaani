package com.example.musify.presentation.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry

fun AnimatedContentTransitionScope<NavBackStackEntry>.homeExitTransition() : ExitTransition {
    return when (initialState.destination.route) {
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
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.homeEnterTransition() : EnterTransition {
    return when (initialState.destination.route) {
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
}