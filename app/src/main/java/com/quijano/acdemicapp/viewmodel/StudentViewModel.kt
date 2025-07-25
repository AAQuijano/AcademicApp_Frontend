package com.quijano.acdemicapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quijano.acdemicapp.data.remote.local.SessionManager
import com.quijano.acdemicapp.network.ApiService
import com.quijano.acdemicapp.network.Grade
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.firstOrNull

class StudentViewModel(
    private val apiService: ApiService,
    private val sessionManager: SessionManager,
    private val userId: String
) : ViewModel() {

    private val _gradesState = MutableStateFlow<GradesState>(GradesState.Loading)
    val gradesState: StateFlow<GradesState> = _gradesState

    val grades = MutableStateFlow<List<Grade>>(emptyList())

    init {
        fetchGrades()
    }

    fun fetchGrades() {
        viewModelScope.launch {
            val token = sessionManager.accessToken.firstOrNull()
            if (token != null) {
                try {
                    val result = apiService.getStudentGrades(userId, token)
                    grades.value = result
                    _gradesState.value = GradesState.Success(result)
                } catch (e: Exception) {
                    _gradesState.value = GradesState.Error(e.message ?: "Error al obtener calificaciones")
                }
            } else {
                _gradesState.value = GradesState.Error("Token no disponible")
            }
        }
    }

    sealed class GradesState {
        object Loading : GradesState()
        data class Success(val grades: List<Grade>) : GradesState()
        data class Error(val message: String) : GradesState()
    }
}