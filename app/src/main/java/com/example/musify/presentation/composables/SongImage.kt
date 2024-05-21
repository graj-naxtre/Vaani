package com.example.musify.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.musify.R

@Composable
fun SongImage(songImage: ByteArray?, modifier: Modifier) {
    val context = LocalContext.current

    val model = remember {
        ImageRequest.Builder(context)
            .data(songImage)
            .crossfade(true)
            .build()
    }

    if (songImage != null) {
        AsyncImage(
            model = model,
            placeholder = painterResource(R.drawable.fallback_image),
            contentDescription = "Song cover",
            contentScale = ContentScale.FillBounds,
            modifier = modifier.aspectRatio(1f / 1f)
        )
    } else {
        Image(painter = painterResource(id = R.drawable.fallback_image), contentDescription = "Song cover")
    }
}