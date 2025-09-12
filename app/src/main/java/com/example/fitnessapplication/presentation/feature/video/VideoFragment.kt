package com.example.fitnessapplication.presentation.feature.video

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.annotation.OptIn
import androidx.core.view.isVisible
import androidx.media3.common.util.UnstableApi
import com.example.fitnessapplication.R
import com.example.fitnessapplication.databinding.FragmentVideoBinding
import com.example.fitnessapplication.domain.util.toMinuteSecondFormat
import com.example.fitnessapplication.presentation.util.applyBottomInsets
import com.example.fitnessapplication.presentation.util.launchOnStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalTime

@AndroidEntryPoint
class VideoFragment : Fragment() {

    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VideoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLayoutParams()
        subscribe()
        setListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @OptIn(UnstableApi::class)
    private fun setupLayoutParams() = with(binding) {
        playerControlLayout.root.applyBottomInsets()
        playerLayout.setAspectRatio(16f/9f)
        viewModel.player.setVideoTextureView(playerView)
    }

    private fun setListeners() = with(binding) {
        playerControlLayout.playButton.setOnClickListener {
            viewModel.switchPlayStatus()
        }
        errorLayout.refreshButton.setOnClickListener {
            viewModel.loadData()
        }

        playerControlLayout.seekBar.setOnSeekBarChangeListener(
            object : OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {}

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    seekBar?.progress?.let { viewModel.seekTo(it) }
                }
            }
        )
    }

    private fun subscribe() = with(binding) {
        launchOnStarted {
            launch {
                viewModel.state.collectLatest {
                    updateState(it)
                }
            }
            launch {
                viewModel.isPlaying.collectLatest {
                    playerControlLayout.playButton.setIconResource(
                        if (it) R.drawable.ic_pause else R.drawable.ic_play
                    )
                }
            }
            launch {
                launch {
                    viewModel.currentTime.collectLatest {
                        playerControlLayout.timeLabel.text = LocalTime.fromMillisecondOfDay(it.toInt()).toMinuteSecondFormat()
                    }
                }
                launch {
                    viewModel.duration.collectLatest {
                        playerControlLayout.durationLabel.text = LocalTime.fromMillisecondOfDay(it.toInt()).toMinuteSecondFormat()
                    }
                }
                launch {
                    viewModel.progress.collectLatest {
                        playerControlLayout.seekBar.progress = it
                    }
                }
            }
        }
    }

    private fun updateState(state: VideoScreenState) = with(binding) {
        playerContent.isVisible = !state.isLoading && state.error == null
        progressIndicator.isVisible = state.isLoading
        errorLayout.root.isVisible = state.error != null
        errorLayout.errorMessage.text = state.error?.message ?: getString(R.string.error_label)
    }
}