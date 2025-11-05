package com.example.jamuin.data.repository

import com.example.jamuin.data.api.ApiService
import com.example.jamuin.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExpertSystemRepository(private val apiService: ApiService) {
    
    suspend fun initializeExpertSystem(): Result<ExpertSystemInitResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.initializeExpertSystem()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to initialize: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun startDiagnosis(): Result<StartDiagnosisResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.startDiagnosis()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to start diagnosis: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun submitAnswer(request: DiagnoseRequest): Result<DiagnoseResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.submitAnswer(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to submit answer: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getAllQuestions(): Result<List<Question>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getAllQuestions()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch questions: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
