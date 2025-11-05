package com.example.jamuin.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jamuin.data.api.RetrofitInstance
import com.example.jamuin.data.model.*
import com.example.jamuin.data.repository.ExpertSystemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ExpertSystemUiState {
    object Initial : ExpertSystemUiState()
    object Loading : ExpertSystemUiState()
    data class Question(val question: com.example.jamuin.data.model.Question, val sessionId: String) : ExpertSystemUiState()
    data class Recommendation(val recommendation: com.example.jamuin.data.model.Recommendation, val sessionId: String) : ExpertSystemUiState()
    data class Error(val message: String) : ExpertSystemUiState()
}

class ExpertSystemViewModel : ViewModel() {
    private val repository = ExpertSystemRepository(RetrofitInstance.apiService)
    
    private val _uiState = MutableStateFlow<ExpertSystemUiState>(ExpertSystemUiState.Initial)
    val uiState: StateFlow<ExpertSystemUiState> = _uiState.asStateFlow()
    
    private val _answeredQuestions = MutableStateFlow<List<Pair<String, String>>>(emptyList())
    val answeredQuestions: StateFlow<List<Pair<String, String>>> = _answeredQuestions.asStateFlow()
    
    fun initializeSystem() {
        viewModelScope.launch {
            _uiState.value = ExpertSystemUiState.Loading
            repository.initializeExpertSystem()
                .onSuccess {
                    // After initialization, start diagnosis
                    startDiagnosis()
                }
                .onFailure { error ->
                    _uiState.value = ExpertSystemUiState.Error(
                        error.message ?: "Failed to initialize system"
                    )
                }
        }
    }
    
    fun startDiagnosis() {
        viewModelScope.launch {
            _uiState.value = ExpertSystemUiState.Loading
            _answeredQuestions.value = emptyList()
            
            repository.startDiagnosis()
                .onSuccess { response ->
                    if (response.nextQuestion != null) {
                        _uiState.value = ExpertSystemUiState.Question(
                            response.nextQuestion,
                            response.sessionId
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.value = ExpertSystemUiState.Error(
                        error.message ?: "Failed to start diagnosis"
                    )
                }
        }
    }
    
    fun submitAnswer(questionId: String, selectedOptionId: String, sessionId: String, questionText: String, answerText: String) {
        viewModelScope.launch {
            _uiState.value = ExpertSystemUiState.Loading
            
            // Add to answered questions
            _answeredQuestions.value = _answeredQuestions.value + Pair(questionText, answerText)
            
            val request = DiagnoseRequest(questionId, selectedOptionId, sessionId)
            repository.submitAnswer(request)
                .onSuccess { response ->
                    if (response.isComplete && response.recommendation != null) {
                        _uiState.value = ExpertSystemUiState.Recommendation(
                            response.recommendation,
                            response.sessionId
                        )
                    } else if (response.nextQuestion != null) {
                        _uiState.value = ExpertSystemUiState.Question(
                            response.nextQuestion,
                            response.sessionId
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.value = ExpertSystemUiState.Error(
                        error.message ?: "Failed to submit answer"
                    )
                }
        }
    }
    
    fun reset() {
        _uiState.value = ExpertSystemUiState.Initial
        _answeredQuestions.value = emptyList()
    }
}
