package com.example.musify.presentation.screens

import android.util.Log
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.musify.R
import com.example.musify.presentation.view_model.SearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(onBackClick: () -> Unit, viewModel: SearchViewModel) {
    val searchResult by viewModel.searchResults.collectAsState()
    LaunchedEffect(key1 = Unit) {
        viewModel.fetchAllSongs()
    }

    Scaffold(
        topBar = {
            SearchScreenHeader(onBackClick = onBackClick, viewModel = viewModel)
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
            LazyColumn {
                items(items = searchResult){
                    Text(text = it.fileName, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun SearchScreenHeader(onBackClick: () -> Unit, viewModel: SearchViewModel) {
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
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "back icon",
                tint = colorResource(id = R.color.orange)
            )
        }
        TextField(
            value = searchText,
            onValueChange = {
                searchText = it;
                if(searchText.isNotEmpty()){
                    coroutineScope.launch {
                        delay(500)
                        viewModel.searchForResults(searchText)
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
                unfocusedContainerColor = Color.Unspecified,
                focusedTextColor = Color.White,
            ),
            modifier = Modifier.weight(9f)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewSearchScreen() {
    SearchScreen(onBackClick = { /*TODO*/ }, viewModel = viewModel<SearchViewModel>())
}