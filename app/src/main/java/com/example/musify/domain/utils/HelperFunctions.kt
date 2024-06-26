package com.example.musify.domain.utils

import java.util.concurrent.TimeUnit

fun Long.formatMinSec(): String {
    return if (this == 0L) {
        "0:00"
    } else {
        String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(this),
            TimeUnit.MILLISECONDS.toSeconds(this) -
                    TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(this)
                    )
        )
    }
}