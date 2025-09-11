package com.example.fitnessapplication.data.mappers

import com.example.fitnessapplication.data.dto.videos.VideoDto
import com.example.fitnessapplication.domain.model.videos.Video

fun VideoDto.toVideo() = Video(
    id = id,
    duration = duration,
    link = link
)