package com.example.fitnessapplication.data.repository

import com.example.fitnessapplication.data.api.WorkoutsApi
import com.example.fitnessapplication.data.mappers.toWorkouts
import com.example.fitnessapplication.domain.model.workouts.Workout
import com.example.fitnessapplication.domain.repository.WorkoutsRepository
import javax.inject.Inject

class WorkoutsRepositoryImpl @Inject constructor(
    private val api: WorkoutsApi
) : WorkoutsRepository{
    override suspend fun getWorkouts(): List<Workout> {
        return api.getWorkouts().toWorkouts()
    }
}