package com.example.fitnessapplication.presentation.feature.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.annotation.OptIn
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.media3.common.util.UnstableApi
import com.example.fitnessapplication.R
import com.example.fitnessapplication.databinding.FragmentVideoBinding
import com.example.fitnessapplication.domain.util.toMinuteSecondFormat
import com.example.fitnessapplication.presentation.util.applyBottomInsets
import com.example.fitnessapplication.presentation.util.applyTopInsets
import com.example.fitnessapplication.presentation.util.getLabel
import com.example.fitnessapplication.presentation.util.launchOnStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
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
        settingsLayout.applyTopInsets()
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

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    seekBar?.let { viewModel.startSeek() }
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    seekBar?.progress?.let { viewModel.seekTo(it) }
                }
            }
        )
        selectQualityButton.setOnClickListener {
            showQualityDialog()
        }
        selectSpeedButton.setOnClickListener {
            showSpeedDialog()
        }
    }

    private fun subscribe() = with(binding) {
        launchOnStarted {
            viewModel.state.collectLatest {
                updateState(it)
            }
        }
    }

    private fun updateState(state: VideoScreenState) = with(binding) {
        playerContent.isVisible = !state.isLoading && state.error == null
        progressIndicator.isVisible = state.isLoading || state.isStreamLoading
        errorLayout.root.isVisible = state.error != null
        errorLayout.errorMessage.text = state.error?.message ?: getString(R.string.error_label)
        playerControlLayout.playButton.setIconResource(
            if (state.isPlaying) R.drawable.ic_pause else R.drawable.ic_play
        )
        playerControlLayout.root.isVisible = !state.isStreamLoading
        playerControlLayout.timeLabel.text = LocalTime.fromMillisecondOfDay(state.currentTime.toInt()).toMinuteSecondFormat()
        playerControlLayout.durationLabel.text = LocalTime.fromMillisecondOfDay(state.duration.toInt()).toMinuteSecondFormat()
        if (!state.isStreamLoading && !state.isProgressInteracting) {
            playerControlLayout.seekBar.setProgress(state.process, false)
        }

        playerControlLayout.speedLabel.text = state.selectedSpeed.getLabel(requireContext())
        playerControlLayout.qualityLabel.text = state.selectedQuality.getLabel(requireContext())
    }

    private fun showQualityDialog() {
        val items = viewModel.state.value.qualityList.map { it.getLabel(requireContext()) }.toTypedArray()
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.video_quality_label)
            .setItems(items) { dialog, which -> viewModel.setQuality(which) }
            .show()
    }

    private fun showSpeedDialog() {
        val items = viewModel.state.value.speedList.map { it.getLabel(requireContext()) }.toTypedArray()
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.video_speed_label)
            .setItems(items) { dialog, which -> viewModel.setSpeed(which) }
            .show()
    }
}