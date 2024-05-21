package com.example.musify.presentation.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.musify.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionsBottomSheet(showOptions: (Boolean) -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(onDismissRequest = { showOptions(false) }, sheetState = sheetState) {
        Column(modifier = Modifier.padding(start = 15.dp, end = 15.dp, bottom = 40.dp)) {
            Row (verticalAlignment = Alignment.CenterVertically){
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.favourite_unfilled),
                        contentDescription = "Favourite Icon",
                        modifier = Modifier.scale(0.8f)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))

                Text(text = "Add to Favourites")
            }
            Row (verticalAlignment = Alignment.CenterVertically){
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.playlist_icon),
                        contentDescription = "playlist icon",
                        modifier = Modifier.scale(0.8f)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))

                Text(text = "Add to Playlist")
            }
        }
    }
}