package com.example.musify.presentation.ui.playlist.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
fun PlaylistOptions(
    showOptions: (Boolean) -> Unit,
    onRemoveFromPlaylist: () -> Unit,
    onAddToFav: () -> Unit,){
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(onDismissRequest = { showOptions(false) }, sheetState = sheetState) {
        Column(
            modifier = Modifier.padding(start = 15.dp, end = 15.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.favourite_unfilled),
                    contentDescription = "Favourite Icon",
                    modifier = Modifier.size(20.dp)
                )
                Text(text = "Add to Favourites")
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(remember {
                        Modifier.clickable { onRemoveFromPlaylist() }
                    })
                    .padding(10.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.playlist_icon),
                    contentDescription = "Favourite Icon",
                    modifier = Modifier.size(20.dp)
                )
                Text(text = "Remove from playlist")
            }
        }
    }
}