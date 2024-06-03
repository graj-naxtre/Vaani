package com.example.musify.domain.player

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.DefaultMediaNotificationProvider

@OptIn(UnstableApi::class)
class CustomMediaNotificationProvider(
    context: Context,
) : DefaultMediaNotificationProvider(context) {

}