package com.example.musify.presentation.ui.navigation

sealed class Destination(val route: String){
    object Home : Destination(route = "home")
    object Playlist: Destination(route = "playlist")
    object Search: Destination(route = "Search")
}