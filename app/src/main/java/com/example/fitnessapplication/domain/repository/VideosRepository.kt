package com.example.fitnessapplication.domain.repository

import com.example.fitnessapplication.domain.model.videos.Video

interface VideosRepository {
    suspend fun getVideoById(videoId: Int): Video
}