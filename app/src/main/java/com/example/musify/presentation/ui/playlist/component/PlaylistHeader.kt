package com.example.musify.presentation.ui.playlist.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.musify.R

@Composable
fun PlaylistHeader(title: String, onBackClick: () -> Unit) {
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
        Text(
            text = title,
            modifier = Modifier.weight(8.5f),
            style = MaterialTheme.typography.titleMedium,
            color = colorResource(id = R.color.orange)
        )
    }
}