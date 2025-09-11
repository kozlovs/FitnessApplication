package com.example.fitnessapplication.domain.repository

import com.example.fitnessapplication.domain.model.workouts.Workout

interface WorkoutsRepository {
    suspend fun getWorkouts(): List<Workout>
}