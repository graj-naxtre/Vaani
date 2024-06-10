package com.example.musify.presentation.ui.playlist.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.musify.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeletePlaylist(show: (Boolean) -> Unit, onDelete: () -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(onDismissRequest = { show(false) }, sheetState = sheetState) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(remember {
                    Modifier.clickable { onDelete() }
                })
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Favourite Icon",
                modifier = Modifier.size(20.dp)
            )
            Text(text = "Delete Playlist")
        }
    }
}