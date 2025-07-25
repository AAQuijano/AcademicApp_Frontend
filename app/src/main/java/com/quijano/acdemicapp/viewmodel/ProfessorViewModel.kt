package com.quijano.acdemicapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quijano.acdemicapp.data.remote.local.SessionManager
import com.quijano.acdemicapp.network.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class ProfessorViewModel(
    private val apiService: ApiService,
    private val sessionManager: SessionManager,
    private val professorId: String
) : ViewModel() {

    private val _scoresState = MutableStateFlow<ScoresState>(ScoresState.Loading)
    val scoresState: StateFlow<ScoresState> = _scoresState

    val scores = MutableStateFlow<List<Score>>(emptyList())

    fun fetchScores() {
        viewModelScope.launch {
            val token = sessionManager.accessToken.firstOrNull()
            if (token != null) {
                try {
                    val result = apiService.getProfessorScores(professorId, token)
                    scores.value = result
                    _scoresState.value = ScoresState.Success(result)
                } catch (e: Exception) {
                    _scoresState.value = ScoresState.Error(e.message ?: "Error al obtener scores")
                }
            } else {
                _scoresState.value = ScoresState.Error("Token no disponible")
            }
        }
    }

    sealed class ScoresState {
        object Loading : ScoresState()
        data class Success(val scores: List<Score>) : ScoresState()
        data class Error(val message: String) : ScoresState()
    }
}