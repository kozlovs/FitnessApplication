package com.example.fitnessapplication.data.mappers

import com.example.fitnessapplication.data.dto.workouts.WorkoutDto
import com.example.fitnessapplication.domain.model.workouts.Workout
import com.example.fitnessapplication.domain.model.workouts.WorkoutType

fun WorkoutDto.toWorkout() = Workout(
    id = id,
    title = title,
    description = description,
    type = type.toWorkoutType(),
    duration = duration
)

fun List<WorkoutDto>.toWorkouts() = map(WorkoutDto::toWorkout)

fun Int.toWorkoutType() = when(this) {
    1 -> WorkoutType.TRAINING
    2 -> WorkoutType.BROADCAST
    3 -> WorkoutType.SET
    else -> WorkoutType.ANOTHER
}