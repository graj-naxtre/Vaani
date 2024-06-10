package com.example.musify.presentation.ui.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.musify.R
import com.example.musify.presentation.composables.SongImage
import com.example.musify.presentation.viewmodels.AudioFileInfo

@Composable
fun SongItem(
    audioFileInfo: AudioFileInfo,
    songClick: () -> Unit,
    optionClick: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val model = remember {
        ImageRequest.Builder(context)
            .data(audioFileInfo.mediaImage)
            .placeholder(R.drawable.fallback_image)
            .fallback(R.drawable.fallback_image)
            .crossfade(true)
            .build()
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                songClick()
            }
            .padding(horizontal = 10.dp, vertical = 15.dp)
    ) {
        Card(
            modifier = Modifier
                .weight(0.2f)
                .padding(10.dp)
                .clip(MaterialTheme.shapes.medium)
        ) {
            AsyncImage(
                model = model,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.aspectRatio(1f / 1f)
            )
        }

        Column(modifier = Modifier.weight(0.7f)) {
            Text(
                text = audioFileInfo.fileName,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = audioFileInfo.artistName!!,
                color = Color.Gray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            )
        }

        IconButton(onClick = { optionClick(true) }, modifier = Modifier.weight(0.1f)) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.three_vertical_dots),
                contentDescription = "More icon",
                tint = Color.White,
                modifier = Modifier.scale(0.8f)
            )
        }
    }
}