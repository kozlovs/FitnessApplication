package com.example.fitnessapplication.domain.util

import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern

private const val SHORT_TIME_SECONDS_PATTERN = "mm:ss"

fun LocalTime.toMinuteSecondFormat() = ofPattern(SHORT_TIME_SECONDS_PATTERN)

private fun LocalTime.ofPattern(pattern: String): String {
    @OptIn(FormatStringsInDatetimeFormats::class)
    return format(LocalTime.Format { byUnicodePattern(pattern) })
}