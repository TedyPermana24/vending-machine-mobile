package com.example.jamuin.data.model

import com.google.gson.annotations.SerializedName

data class ApiError(
    @SerializedName("statusCode")
    val statusCode: Int,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("error")
    val error: String
)
