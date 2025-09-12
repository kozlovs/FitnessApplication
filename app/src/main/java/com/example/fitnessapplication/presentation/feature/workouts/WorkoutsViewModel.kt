package com.example.fitnessapplication.presentation.feature.workouts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnessapplication.domain.model.workouts.WorkoutType
import com.example.fitnessapplication.domain.repository.WorkoutsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutsViewModel @Inject constructor(
    private val workoutsRepository: WorkoutsRepository
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val typeFilter = MutableStateFlow<WorkoutType?>(null)

    private val _state = MutableStateFlow(WorkoutsScreenState())
    val state = combine(
        _state,
        searchQuery,
        typeFilter
    ) { state, searchQuery, typeFilter ->
        state.copy(
            workouts = state.workouts.filter { workout ->
                val searched = searchQuery.isBlank() || (workout.title.contains(searchQuery, true))
                val filtered = typeFilter?.let { workout.type == it } ?: true
                searched && filtered
            }
        )
    }

    init {
        loadData()
    }

    fun loadData() = viewModelScope.launch {
        try {
            _state.update { it.copy(isLoading = true, error = null) }
            val workouts = workoutsRepository.getWorkouts()
            _state.update { it.copy(isLoading = false, workouts = workouts) }
        } catch (e: Exception) {
            _state.update {
                it.copy(
                    isLoading = false,
                    error = e
                )
            }
        }
    }

    fun refreshData() = viewModelScope.launch {
        try {
            _state.update { it.copy(isRefreshing = true, error = null) }
            val workouts = workoutsRepository.getWorkouts()
            _state.update { it.copy(isRefreshing = false, workouts = workouts) }
        } catch (e: Exception) {
            _state.update {
                it.copy(
                    isRefreshing = false,
                    error = e
                )
            }
        }
    }

    fun setSearchQuery(newSearchQuery: String) {
        searchQuery.update { newSearchQuery }
    }

    fun setTypeFilter(newTypeFilter: WorkoutType?) {
        typeFilter.update { newTypeFilter }
    }
}