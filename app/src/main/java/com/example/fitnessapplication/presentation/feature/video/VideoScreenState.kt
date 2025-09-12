package com.example.fitnessapplication.presentation.feature.video

import com.example.fitnessapplication.domain.model.videos.Video

data class VideoScreenState(
    val video: Video? = null,
    val isPlaying: Boolean = false,
    val isLoading: Boolean = true,
    val error: Exception? = null
)