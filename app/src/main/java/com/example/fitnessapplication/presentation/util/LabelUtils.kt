package com.example.fitnessapplication.presentation.util

import android.content.Context
import com.example.fitnessapplication.domain.model.workouts.WorkoutType

fun WorkoutType.getLabel(context: Context) = when(this) {
    WorkoutType.TRAINING -> "Тренировка"
    WorkoutType.BROADCAST -> "Эфир"
    WorkoutType.SET -> "Комплекс"
    WorkoutType.ANOTHER -> "Другое"
}