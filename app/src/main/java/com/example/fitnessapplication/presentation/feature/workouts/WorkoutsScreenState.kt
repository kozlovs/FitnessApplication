package com.example.fitnessapplication.presentation.feature.workouts

import com.example.fitnessapplication.domain.model.workouts.Workout

data class WorkoutsScreenState(
    val workouts: List<Workout> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: String? = null
)