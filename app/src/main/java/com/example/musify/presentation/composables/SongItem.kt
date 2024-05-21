package com.example.musify.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musify.R
import com.example.musify.presentation.view_model.AudioFileInfo
import com.example.musify.presentation.view_model.HomeScreenViewModel

@Composable
fun SongItem(
    songIndex: Int,
    audioFileInfo: AudioFileInfo,
    viewModel: HomeScreenViewModel,
    showMiniPlayer: () -> Unit,
    songClick: (Int) -> Unit,
    optionClick: (Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                showMiniPlayer()
                viewModel.setMediaItem(audioFileInfo)
                viewModel.myAudioPlayer.playSong(audioFileInfo, songIndex)
                songClick(songIndex)
            }
            .padding(horizontal = 10.dp, vertical = 15.dp)
    ) {
        Card(
            modifier = Modifier
                .weight(0.2f)
                .padding(10.dp)
                .clip(MaterialTheme.shapes.medium)
        ) {
            SongImage(songImage = audioFileInfo.mediaImage, modifier = Modifier)
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