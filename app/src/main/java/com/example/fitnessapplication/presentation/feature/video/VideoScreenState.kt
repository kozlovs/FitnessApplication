package com.example.fitnessapplication.presentation.feature.video

import com.example.fitnessapplication.domain.model.videos.Video
import com.example.fitnessapplication.domain.model.videos.VideoQuality
import com.example.fitnessapplication.domain.model.videos.VideoSpeed

data class VideoScreenState(
    val video: Video? = null,
    val isPlaying: Boolean = false,
    val isLoading: Boolean = true,
    val duration: Long = 0L,
    val currentTime: Long = 0L,
    val process: Int = 0,
    val isProgressInteracting: Boolean = false,
    val isStreamLoading: Boolean = true,
    val selectedQuality: VideoQuality = VideoQuality.AUTO,
    val qualityList: List<VideoQuality> = VideoQuality.entries,
    val selectedSpeed: VideoSpeed = VideoSpeed.X1,
    val speedList: List<VideoSpeed> = VideoSpeed.entries,
    val error: Exception? = null
)