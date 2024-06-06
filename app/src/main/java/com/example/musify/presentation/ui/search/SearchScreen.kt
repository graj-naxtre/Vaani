package com.example.musify.presentation.ui.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.musify.R
import com.example.musify.presentation.ui.search.component.SearchItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchScreen(viewModel: SearchViewModel, onBackClick: () -> Unit) {
    val searchResult by viewModel.searchResults.collectAsState()
    val recentSearches by remember {
        mutableStateOf(viewModel.recentSearches)
    }

    Scaffold(
        topBar = {
            SearchScreenHeader(onBackClick = onBackClick, onEvent = viewModel::onEvent)
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        listOf(
                            colorResource(id = R.color.top_color),
                            colorResource(id = R.color.bottom_color)
                        )
                    )
                )
                .padding(paddingValues)
        ) {
            if (searchResult.isNotEmpty()) {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    stickyHeader {
                        Text(
                            text = "Results",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White
                        )
                    }
                    items(items = searchResult) {
                        SearchItem(audioFileInfo = it) {
                            viewModel.onEvent(SearchEvent.OnItemClick(it))
                        }
                    }
                }
            }
            if (recentSearches.isNotEmpty()) {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    stickyHeader {
                        Text(
                            text = "Recent Searches",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White
                        )
                    }
                    items(items = recentSearches) {
                        SearchItem(audioFileInfo = it, isRecentSearch = true, onClear = {}) {

                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchScreenHeader(onBackClick: () -> Unit, onEvent: (SearchEvent) -> Unit) {
    var searchText by remember {
        mutableStateOf("")
    }
    val coroutineScope = rememberCoroutineScope()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        modifier = Modifier
            .background(colorResource(id = R.color.top_color))
            .padding(10.dp)
    ) {
        IconButton(
            onClick = { onBackClick() },
            modifier = Modifier
                .weight(1.5f)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "back icon",
                tint = colorResource(id = R.color.orange)
            )
        }
        TextField(
            value = searchText,
            onValueChange = {
                searchText = it;
                if (searchText.length > 2) {
                    coroutineScope.launch {
                        delay(500)
                        onEvent(SearchEvent.OnSearch(searchText))
                    }
                }
            },
            placeholder = {
                Text(
                    text = "Search Song",
                    color = colorResource(id = R.color.orange)
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search icon",
                    tint = colorResource(id = R.color.orange),
                )
            },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor = Color.White,
                focusedIndicatorColor = colorResource(id = R.color.orange),
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier.weight(8.5f)
        )
    }
}