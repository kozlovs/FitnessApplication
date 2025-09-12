package com.example.fitnessapplication.data.api

import com.example.fitnessapplication.data.dto.videos.VideoDto
import retrofit2.http.GET
import retrofit2.http.Query

interface VideosApi {
    @GET("/get_video")
    suspend fun getVideoById(
        @Query("id") videoId: Int
    ): VideoDto
}