package com.example.musify.presentation.ui.search.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.musify.R
import com.example.musify.presentation.viewmodels.AudioFileInfo

@Composable
fun SearchItem(
    audioFileInfo: AudioFileInfo,
    isRecentSearch: Boolean = false,
    onClear: () -> Unit = {},
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val model = remember(audioFileInfo.mediaImage) {
        ImageRequest.Builder(context)
            .data(audioFileInfo.mediaImage)
            .fallback(R.drawable.fallback_image)
            .placeholder(R.drawable.fallback_image)
            .build()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .then(remember {
                Modifier.clickable { }
            }),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        AsyncImage(
            model = model,
            contentDescription = "song image",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .weight(0.15f)
                .aspectRatio(1f)
                .size(40.dp),
            clipToBounds = true,
        )
        Column(modifier = Modifier.weight(0.7f)) {
            Text(
                text = audioFileInfo.fileName,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
            Text(
                text = audioFileInfo.artistName ?: "Unknown",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
        if (isRecentSearch) {
            IconButton(onClick = { onClear() }, modifier = Modifier.weight(0.15f)) {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "clear icon",
                    tint = Color.Gray
                )
            }
        }
    }
}