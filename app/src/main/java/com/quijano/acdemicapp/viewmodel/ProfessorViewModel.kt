package com.quijano.acdemicapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quijano.acdemicapp.network.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfessorViewModel(
    private val apiService: ApiService,
    private val professorId: String
) : ViewModel() {

    private val _scoresState = MutableStateFlow<ScoresState>(ScoresState.Loading)
    val scoresState: StateFlow<ScoresState> = _scoresState

    private val _createScoreState = MutableStateFlow<CreateScoreState>(CreateScoreState.Idle)
    val createScoreState: StateFlow<CreateScoreState> = _createScoreState

    private val _createGradeState = MutableStateFlow<CreateGradeState>(CreateGradeState.Idle)
    val createGradeState: StateFlow<CreateGradeState> = _createGradeState

    init {
        fetchScores()
    }

    fun fetchScores() {
        viewModelScope.launch {
            try {
                val scores = apiService.getProfessorScores(professorId)
                _scoresState.value = ScoresState.Success(scores)
            } catch (e: Exception) {
                _scoresState.value = ScoresState.Error(e.message ?: "Error al obtener scores")
            }
        }
    }

    fun createScore(scoreRequest: ScoreRequest) {
        _createScoreState.value = CreateScoreState.Loading
        viewModelScope.launch {
            try {
                apiService.createScore(scoreRequest)
                _createScoreState.value = CreateScoreState.Success
                fetchScores() // Refresh the list
            } catch (e: Exception) {
                _createScoreState.value = CreateScoreState.Error(e.message ?: "Error al crear score")
            }
        }
    }

    fun createGrade(gradeRequest: GradeRequest) {
        _createGradeState.value = CreateGradeState.Loading
        viewModelScope.launch {
            try {
                apiService.createGrade(gradeRequest)
                _createGradeState.value = CreateGradeState.Success
            } catch (e: Exception) {
                _createGradeState.value = CreateGradeState.Error(e.message ?: "Error al crear grade")
            }
        }
    }

    sealed class ScoresState {
        object Loading : ScoresState()
        data class Success(val scores: List<Score>) : ScoresState()
        data class Error(val message: String) : ScoresState()
    }

    sealed class CreateScoreState {
        object Idle : CreateScoreState()
        object Loading : CreateScoreState()
        object Success : CreateScoreState()
        data class Error(val message: String) : CreateScoreState()
    }

    sealed class CreateGradeState {
        object Idle : CreateGradeState()
        object Loading : CreateGradeState()
        object Success : CreateGradeState()
        data class Error(val message: String) : CreateGradeState()
    }


    val scores: StateFlow<List<Score>> = _scoresState
        .map { state ->
            when (state) {
                is ScoresState.Success -> state.scores
                else -> emptyList()
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

}