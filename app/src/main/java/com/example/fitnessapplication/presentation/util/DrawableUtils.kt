package com.example.fitnessapplication.presentation.util

import com.example.fitnessapplication.R
import com.example.fitnessapplication.domain.model.workouts.WorkoutType

fun WorkoutType.getIconRes() = when(this) {
    WorkoutType.TRAINING -> R.drawable.ic_training
    WorkoutType.BROADCAST -> R.drawable.ic_broadcast
    WorkoutType.SET -> R.drawable.ic_set
    WorkoutType.ANOTHER -> R.drawable.ic_empty
}