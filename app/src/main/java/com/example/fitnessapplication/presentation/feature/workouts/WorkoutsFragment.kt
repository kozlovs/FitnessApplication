package com.example.fitnessapplication.presentation.feature.workouts

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnessapplication.R
import com.example.fitnessapplication.databinding.FragmentWorkoutsBinding
import com.example.fitnessapplication.domain.model.workouts.WorkoutType
import com.example.fitnessapplication.presentation.util.applyBottomInsets
import com.example.fitnessapplication.presentation.util.applyTopInsets
import com.example.fitnessapplication.presentation.util.launchOnStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class WorkoutsFragment : Fragment() {

    private var workoutsAdapter: WorkoutsAdapter? = null
    private var _binding: FragmentWorkoutsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WorkoutsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLayoutParams()
        setupAdapters()
        subscribe()
        setListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        workoutsAdapter = null
    }

    private fun setupLayoutParams() = with(binding) {
        appbarLayout.applyTopInsets()
        workouts.applyBottomInsets()
    }

    private fun setupAdapters() = with(binding) {
        workoutsAdapter = WorkoutsAdapter {
            findNavController().navigate(
                R.id.action_workoutsFragment_to_videoFragment,
                bundleOf("workoutId" to it.id)
            )
        }
        workouts.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = workoutsAdapter
        }
    }

    private fun setListeners() = with(binding) {
        errorLayout.refreshButton.setOnClickListener {
            viewModel.loadData()
        }
        swipeRefresh.setOnRefreshListener { viewModel.refreshData() }
        workoutTypeChipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            when (checkedIds.firstOrNull()) {
                R.id.workout_type_training -> WorkoutType.TRAINING
                R.id.workout_type_broadcast -> WorkoutType.BROADCAST
                R.id.workout_type_set -> WorkoutType.SET
                else -> null
            }.let { viewModel.setTypeFilter(it) }
        }
        etSearch.doAfterTextChanged {
            it?.let { viewModel.setSearchQuery(it.toString()) }
        }
    }

    private fun subscribe() {
        launchOnStarted {
            viewModel.state.collectLatest {
                updateState(it)
            }
        }
    }

    private fun updateState(state: WorkoutsScreenState) = with(binding) {
        emptyLayout.root.isVisible =
            state.workouts.isEmpty() && state.error == null && state.isLoading.not()
        workoutsAdapter?.submitList(state.workouts)
        progressIndicator.isVisible = state.isLoading
        swipeRefresh.isRefreshing = state.isRefreshing
        errorLayout.root.isVisible = state.error != null
        errorLayout.errorMessage.text = state.error?.message ?: getString(R.string.error_label)
    }
}