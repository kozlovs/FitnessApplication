package com.example.fitnessapplication.presentation.feature.workouts

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnessapplication.databinding.FragmentWorkoutsBinding
import com.example.fitnessapplication.presentation.util.launchOnStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class WorkoutsFragment : Fragment() {

    companion object {
        fun newInstance() = WorkoutsFragment()
    }

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
        setupAdapters()
        subscribe()
        setListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        workoutsAdapter = null
    }

    private fun setupAdapters() = with(binding) {
        workoutsAdapter = WorkoutsAdapter {

        }
        workouts.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = workoutsAdapter
        }
    }

    private fun setListeners() = with(binding) {
        swipeRefresh.setOnRefreshListener { viewModel.refreshData() }
    }

    private fun subscribe() {
        launchOnStarted {
            viewModel.state.collectLatest {
                updateState(it)
            }
        }
    }

    private fun updateState(state: WorkoutsScreenState) = with(binding) {
        emptyLayout.root.isVisible = state.workouts.isEmpty() && state.error == null && state.isLoading.not()
        workoutsAdapter?.submitList(state.workouts)
        progressIndicator.isVisible = state.isLoading
        swipeRefresh.isRefreshing = state.isRefreshing
    }
}