package com.example.fitnessapplication.domain.model.videos

enum class VideoQuality(val width: Int, val height: Int) {
    HD(1280, 720), FUL_HD(1920, 1080), AUTO(0, 0)
}