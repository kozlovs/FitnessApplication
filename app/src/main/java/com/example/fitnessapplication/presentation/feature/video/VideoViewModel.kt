package com.example.fitnessapplication.presentation.feature.video

import androidx.annotation.OptIn
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.example.fitnessapplication.BuildConfig
import com.example.fitnessapplication.domain.model.videos.Video
import com.example.fitnessapplication.domain.repository.VideosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class VideoViewModel @Inject constructor(
    private val videosRepository: VideosRepository,
    val player: ExoPlayer,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val workoutId = VideoFragmentArgs.fromSavedStateHandle(savedStateHandle).workoutId

    private val _state = MutableStateFlow(VideoScreenState())
    val state = _state.asStateFlow()

    init {
        loadData()
        observePlayerState()
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
            val video = videosRepository.getVideoById(workoutId)
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
        val url = BuildConfig.BASE_URL + video.link
        player.addMediaItem(MediaItem.fromUri(url.toUri()))
    }

    private fun observePlayerState() {
        player.addListener(
            object : Player.Listener {
                override fun onIsLoadingChanged(isLoading: Boolean) {
                    _state.update {
                        val duration = player.duration.takeIf { duration -> duration > 0 } ?: 0
                        it.copy(
                            duration = duration,
                            isStreamLoading = isLoading
                        )
                    }
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    _state.update {
                        it.copy(
                            isPlaying = isPlaying
                        )
                    }
                }

                override fun onPlayerError(error: PlaybackException) {
                    super.onPlayerError(error)
                    _state.update {
                        it.copy(
                            error = error,
                            isLoading = false,
                            isStreamLoading = false,
                            isPlaying = false
                        )
                    }
                }
            }
        )
    }

    fun switchPlayStatus() {
        if (player.isPlaying) pause()
        else play()
    }

    private fun play() {
        player.play()
        _state.update { it.copy(isPlaying = true) }
    }

    private fun pause() {
        player.pause()
        _state.update { it.copy(isPlaying = false) }
    }

    fun startSeek() {
        _state.update { it.copy(isProgressInteracting = true) }
    }

    fun setQuality(position: Int) {
        _state.update {
            val newQuality = it.qualityList[position]
            val params = player.trackSelectionParameters
                .buildUpon()
                .setMaxVideoSize(newQuality.width, newQuality.height)
                .setMinVideoSize(newQuality.width, newQuality.height)
                .build()
            player.trackSelectionParameters = params
            it.copy(selectedQuality = newQuality)
        }
    }

    fun setSpeed(position: Int) {
        _state.update {
            val newSpeed = it.speedList[position]
            player.setPlaybackSpeed(newSpeed.value)
            it.copy(selectedSpeed = newSpeed)
        }
    }

    fun seekTo(positionPercentage: Int) {
        val duration = player.duration
        val positionMs = positionPercentage * duration / 100
        player.seekTo(positionMs)
        _state.update { it.copy(isProgressInteracting = true) }
    }

    private fun launchTimeObserver() = viewModelScope.launch {
        while(true) {
            refreshPlayerState()
            delay(100)
        }
    }

    @OptIn(UnstableApi::class)
    private fun refreshPlayerState() {
        val duration = player.duration.takeIf { it > 0L } ?: 0L
        val currentTime = player.currentPosition.takeIf { it > 0L } ?: 0L
        val progress = if (duration <= 0) 0 else currentTime * 100 / duration
        _state.update {
            it.copy(
                currentTime = currentTime,
                process = progress.toInt()
            )
        }
    }
}