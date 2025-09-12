package com.example.fitnessapplication.presentation.util

import android.content.Context
import com.example.fitnessapplication.R
import com.example.fitnessapplication.domain.model.workouts.WorkoutType

fun WorkoutType.getLabel(context: Context) = when(this) {
    WorkoutType.TRAINING -> R.string.workout_type_training
    WorkoutType.BROADCAST -> R.string.workout_type_broadcast
    WorkoutType.SET -> R.string.workout_type_set
    WorkoutType.ANOTHER -> R.string.workout_type_another
}

fun Int.minutesToDuration(context: Context) = context.getString(R.string.duration_minute_label, this)