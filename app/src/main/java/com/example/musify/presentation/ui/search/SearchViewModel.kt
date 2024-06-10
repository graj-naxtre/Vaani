package com.example.musify.presentation.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musify.data.model.AppFolderWithSongs
import com.example.musify.data.model.FolderWithSongs
import com.example.musify.domain.repository.MusicRepository
import com.example.musify.presentation.viewmodels.AudioFileInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(val musicRepository: MusicRepository) : ViewModel() {
    private val _allMedia = mutableSetOf<AudioFileInfo>()
    private val allMedia: Set<AudioFileInfo> = _allMedia

    private val _searchResults = MutableStateFlow<List<AudioFileInfo>>(emptyList())
    val searchResults: StateFlow<List<AudioFileInfo>> = _searchResults.asStateFlow()

    private val _recentSearches = mutableSetOf<AudioFileInfo>()
    val recentSearches: List<AudioFileInfo> = _recentSearches.toList()

    init {
        fetchAllSongs()
    }

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnSearch -> searchForResults(event.input)
            is SearchEvent.OnItemClick -> {
                addRecentSearch(event.audioFileInfo)
            }
        }
    }

    private fun fetchAllSongs() {
        viewModelScope.launch {
            musicRepository.getAllMediaSongs()
                .map { folderWithSongs: FolderWithSongs ->
                    folderWithSongs.toAppFolderWithSongs()
                }
                .also { data: List<AppFolderWithSongs> ->
                    data.map { (_, songFiles) ->
                        songFiles.map { eachSong ->
                            _allMedia.add(eachSong)
                        }
                    }
                }
        }
    }

    fun searchForResults(searchSong: String) {
        viewModelScope.launch {
            // clear previous search results
            _searchResults.value = emptyList()

            // perform the search and update the results
            val results = allMedia.filter { audioFileInfo ->
                audioFileInfo.fileName.contains(searchSong, ignoreCase = true)
            }
            _searchResults.value = results.distinct()
        }
    }

    private fun addRecentSearch(audioFileInfo: AudioFileInfo) {
        if (_recentSearches.size >= 20) {
            // Remove the oldest element
            val oldestElement = _recentSearches.first()
            _recentSearches.remove(oldestElement)
        }
        _recentSearches.add(audioFileInfo)
    }
}