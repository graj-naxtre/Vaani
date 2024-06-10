package com.example.musify.presentation.ui.playlist.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.musify.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaylistItem(playlistName: String, onClick: () -> Unit, onLongClick: () -> Unit = {}) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.DarkGray, shape = RoundedCornerShape(5.dp))
            .then(remember {
                Modifier.combinedClickable(onClick = { onClick() }, onLongClick = { onLongClick() })
            })
            .padding(vertical = 10.dp, horizontal = 15.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(Color.Gray, MaterialTheme.shapes.small)
        ) {
            Image(
                painter = painterResource(id = R.drawable.fallback_image),
                contentDescription = "Song cover",
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center,
                modifier = Modifier.size(45.dp)
            )
        }
        Text(text = playlistName, style = MaterialTheme.typography.titleSmall)
    }
}