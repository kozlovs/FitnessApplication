package com.example.fitnessapplication.presentation.util

import android.content.Context
import com.example.fitnessapplication.R

fun String.minutesToDuration(context: Context) = context.getString(R.string.duration_minute_label, this)