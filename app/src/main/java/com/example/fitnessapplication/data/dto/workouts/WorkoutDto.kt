package com.example.fitnessapplication.data.dto.workouts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutDto(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String?,
    @SerialName("type") val type: Int,
    @SerialName("duration") val duration: String
)