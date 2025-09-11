package com.example.fitnessapplication.presentation.feature.workouts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnessapplication.domain.repository.WorkoutsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutsViewModel @Inject constructor(
    private val workoutsRepository: WorkoutsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(WorkoutsScreenState())
    val state = _state.asStateFlow()

    init {
        loadData()
    }

    fun loadData() = viewModelScope.launch {
        try {
            _state.update { it.copy(isLoading = true, error = null) }
            val workouts = workoutsRepository.getWorkouts()
            _state.update { it.copy(isLoading = true, workouts = workouts) }
        } catch (e: Exception) {
            _state.update { it.copy(
                isLoading = false,
                error = e.message ?: "Неизвестная ошибка"
            ) }
        }
    }

    fun refreshData() = viewModelScope.launch {
        try {
            _state.update { it.copy(isRefreshing = true, error = null) }
            val workouts = workoutsRepository.getWorkouts()
            _state.update { it.copy(isRefreshing = true, workouts = workouts) }
        } catch (e: Exception) {
            _state.update { it.copy(
                isRefreshing = false,
                error = e.message ?: "Неизвестная ошибка"
            ) }
        }
    }
}