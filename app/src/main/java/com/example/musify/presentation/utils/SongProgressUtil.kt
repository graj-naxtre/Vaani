package com.example.musify.presentation.utils

import androidx.media3.common.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * An extension method used to get a [flow] of [Long]s representing the
 * current playback progress. If the player is not playing anything, then
 * the flow will end immediately.
 */
fun Player.getCurrentPlaybackProgressFlow() = flow {
    if (!isPlaying) return@flow
    while (currentPosition <= duration) {
        emit(currentPosition)
        delay(1_000)
    }
    emit(currentPosition)
    // when paused, the same value will be emitted, to prevent the
    // emission of the same value, use distinctUntilChanged
}.distinctUntilChanged()

