package com.example.fitnessapplication.data.api

import com.example.fitnessapplication.data.dto.workouts.WorkoutDto
import retrofit2.http.GET

interface WorkoutsApi {

    @GET("get_workouts")
    suspend fun getWorkouts(): List<WorkoutDto>
}