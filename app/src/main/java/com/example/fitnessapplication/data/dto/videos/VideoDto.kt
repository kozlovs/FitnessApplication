package com.example.fitnessapplication.data.dto.videos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoDto(
    @SerialName("id") val id: Int,
    @SerialName("duration") val duration: String,
    @SerialName("link") val link: String
)