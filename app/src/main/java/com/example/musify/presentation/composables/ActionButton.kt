package com.example.musify.presentation.composables

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.musify.R

@Composable
fun ActionButton() {
    FloatingActionButton(onClick = { /*TODO*/ }, shape = CircleShape, containerColor = colorResource(
        id = R.color.orange
    )) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.shuffle_outer),
            contentDescription = "Shuffle Icon",
            tint = Color.White,
            modifier = Modifier.height(20.dp)
        )
    }
}