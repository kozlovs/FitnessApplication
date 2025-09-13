package com.example.fitnessapplication.presentation.util

import android.content.Context
import com.example.fitnessapplication.R
import com.example.fitnessapplication.domain.model.videos.VideoQuality
import com.example.fitnessapplication.domain.model.videos.VideoSpeed

fun String.minutesToDuration(context: Context) = context.getString(R.string.duration_minute_label, this)

fun VideoSpeed.getLabel(context: Context) = when(this) {
    VideoSpeed.X1 -> R.string.video_speed_x1
    VideoSpeed.X2 -> R.string.video_speed_x2
}.let { context.getString(it) }

fun VideoQuality.getLabel(context: Context) = when(this) {
    VideoQuality.HD -> R.string.video_quality_hd
    VideoQuality.FUL_HD -> R.string.video_quality_full_hd
    VideoQuality.AUTO -> R.string.video_quality_auto
}.let { context.getString(it) }