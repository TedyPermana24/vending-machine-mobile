package com.example.jamuin.data.model

import com.google.gson.annotations.SerializedName

data class Question(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("text")
    val text: String,
    
    @SerializedName("options")
    val options: List<QuestionOption>
)

data class QuestionOption(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("text")
    val text: String
)

data class StartDiagnosisResponse(
    @SerializedName("isComplete")
    val isComplete: Boolean,
    
    @SerializedName("sessionId")
    val sessionId: String,
    
    @SerializedName("nextQuestion")
    val nextQuestion: Question?
)

data class DiagnoseRequest(
    @SerializedName("questionId")
    val questionId: String,
    
    @SerializedName("selectedOptionId")
    val selectedOptionId: String,
    
    @SerializedName("sessionId")
    val sessionId: String
)

data class DiagnoseResponse(
    @SerializedName("isComplete")
    val isComplete: Boolean,
    
    @SerializedName("sessionId")
    val sessionId: String,
    
    @SerializedName("nextQuestion")
    val nextQuestion: Question? = null,
    
    @SerializedName("recommendation")
    val recommendation: Recommendation? = null
)

data class Recommendation(
    @SerializedName("productId")
    val productId: Int,
    
    @SerializedName("productName")
    val productName: String,
    
    @SerializedName("alasan")
    val alasan: String
)

data class ExpertSystemInitResponse(
    @SerializedName("message")
    val message: String,
    
    @SerializedName("totalQuestions")
    val totalQuestions: Int,
    
    @SerializedName("totalRules")
    val totalRules: Int
)
