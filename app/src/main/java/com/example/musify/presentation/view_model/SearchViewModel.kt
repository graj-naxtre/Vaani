package com.example.musify.presentation.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musify.data.model.AppFolderWithSongs
import com.example.musify.data.model.FolderWithSongs
import com.example.musify.domain.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(val musicRepository: MusicRepository) : ViewModel() {
    private val _allMedia = mutableListOf<AudioFileInfo>()
    private val allMedia: List<AudioFileInfo> = _allMedia

    private val _searchResults = MutableStateFlow<List<AudioFileInfo>>(emptyList())
    val searchResults: StateFlow<List<AudioFileInfo>> = _searchResults.asStateFlow()

    fun fetchAllSongs() {
        viewModelScope.launch(Dispatchers.IO) {
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
        viewModelScope.launch(Dispatchers.IO) {
            // clear previous search results
            _searchResults.value = emptyList()

            // perform the search and update the results
            val results = allMedia.filter { audioFileInfo ->
                audioFileInfo.fileName.contains(searchSong, ignoreCase = true)
            }
            _searchResults.value = results.distinct()
        }
    }
}