package com.quijano.acdemicapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quijano.acdemicapp.network.ApiService
import com.quijano.acdemicapp.network.Grade
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StudentViewModel(
    private val apiService: ApiService,
    private val userId: String
) : ViewModel() {

    private val _gradesState = MutableStateFlow<GradesState>(GradesState.Loading)
    val gradesState: StateFlow<GradesState> = _gradesState

    init {
        fetchGrades()
    }

    fun fetchGrades() {
        viewModelScope.launch {
            try {
                val grades = apiService.getStudentGrades(userId)
                _gradesState.value = GradesState.Success(grades)
            } catch (e: Exception) {
                _gradesState.value = GradesState.Error(e.message ?: "Error al obtener calificaciones")
            }
        }
    }

    sealed class GradesState {
        object Loading : GradesState()
        data class Success(val grades: List<Grade>) : GradesState()
        data class Error(val message: String) : GradesState()
    }

    val grades: StateFlow<List<Grade>> = _gradesState
        .map { state ->
            when (state) {
                is GradesState.Success -> state.grades
                else -> emptyList()
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

}

