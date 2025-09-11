package com.example.fitnessapplication.data.repository

import com.example.fitnessapplication.data.api.VideosApi
import com.example.fitnessapplication.data.mappers.toVideo
import com.example.fitnessapplication.domain.model.videos.Video
import com.example.fitnessapplication.domain.repository.VideosRepository
import javax.inject.Inject

class VideosRepositoryImpl @Inject constructor(
    private val api: VideosApi
): VideosRepository {
    override suspend fun getVideoById(videoId: Int): Video {
        return api.getVideoById(videoId).toVideo()
    }
}