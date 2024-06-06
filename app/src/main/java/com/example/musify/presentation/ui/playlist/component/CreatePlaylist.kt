package com.example.musify.presentation.ui.playlist.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.musify.R

@Composable
fun CreatePlaylist(onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .background(
                color = colorResource(id = R.color.orange).copy(alpha = 0.8f),
                shape = RoundedCornerShape(5.dp)
            )
            .padding(vertical = 10.dp, horizontal = 15.dp)
            .clickable {
                onClick()
            }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(Color.DarkGray, MaterialTheme.shapes.small)
                .padding(10.dp)
        ) {
            Image(
                imageVector = Icons.Filled.Add,
                contentDescription = "add icon",
                colorFilter = ColorFilter.tint(colorResource(id = R.color.orange))
            )
        }
        Text(
            text = "Create Playlist",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewCreate() {
    CreatePlaylist {

    }
}