package com.example.musify.presentation.ui.navigation

import com.example.musify.presentation.viewmodels.AudioFileInfo

sealed class Destination(val route: String){
    data object Home : Destination(route = "home")
    data object Playlist: Destination(route = "playlist")
    data object Search: Destination(route = "search")
    data object SelectedPlaylist : Destination(route = "selected_playlist"){
        fun setRoute(playlistId: Long) = "${route}/${playlistId}"
    }
    data object AddToPlaylist : Destination(route = "add_to_playlist")
}