package com.example.fitnessapplication.presentation.feature.video

import androidx.annotation.OptIn
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.example.fitnessapplication.domain.model.videos.Video
import com.example.fitnessapplication.domain.repository.VideosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.core.net.toUri
import com.example.fitnessapplication.BuildConfig
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val videosRepository: VideosRepository,
    val player: ExoPlayer,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val workoutId = savedStateHandle.get<Int>("workoutId")

    private val _state = MutableStateFlow(VideoScreenState())
    val state = _state.asStateFlow()

    val isPlaying = MutableStateFlow(false)
    val currentTime = MutableStateFlow(0L)
    val duration = MutableStateFlow(0L)
    val progress = combine(
        currentTime,
        duration
    ) { currentTime, duration ->
        if (duration <= 0) 0
        else currentTime * 100 / duration
    }.map { it.toInt() }


    init {
        loadData()
        player.prepare()
        launchTimeObserver()
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }

    fun loadData() = viewModelScope.launch {
        try {
            _state.update { it.copy(isLoading = true, error = null) }
            val video = videosRepository.getVideoById(workoutId!!)
            setVideo(video)
            _state.update { it.copy(isLoading = false, video = video) }
        } catch (e: Exception) {
            _state.update {
                it.copy(
                    isLoading = false,
                    error = e
                )
            }
        }
    }

    private fun setVideo(video: Video) {
        player.addMediaItem(MediaItem.fromUri((BuildConfig.BASE_URL + video.link).toUri()))
    }

    fun switchPlayStatus() {
        if (player.isPlaying) pause()
        else play()
    }

    fun play() {
        player.play()
        isPlaying.value = true
    }

    fun pause() {
        player.pause()
        isPlaying.value = false
    }

    fun seekTo(positionPercentage: Int) {
        val duration = player.duration
        val positionMs = positionPercentage * duration / 100
        player.seekTo(positionMs)
    }

    private fun launchTimeObserver() = viewModelScope.launch {
        while(true) {
            refreshPlayerState()
            delay(1000)
        }
    }

    @OptIn(UnstableApi::class)
    private fun refreshPlayerState() {
        currentTime.value = player.currentPosition
        duration.value = player.duration.takeIf { it > 0L } ?: 0L
    }
}