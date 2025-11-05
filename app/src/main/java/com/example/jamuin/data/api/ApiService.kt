package com.example.jamuin.data.api

import com.example.jamuin.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    // Products API
    @GET("products")
    suspend fun getAllProducts(): Response<List<Product>>
    
    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Response<Product>
    
    // Expert System API
    @POST("expert-system/initialize")
    suspend fun initializeExpertSystem(): Response<ExpertSystemInitResponse>
    
    @GET("expert-system/start")
    suspend fun startDiagnosis(): Response<StartDiagnosisResponse>
    
    @POST("expert-system/diagnose")
    suspend fun submitAnswer(@Body request: DiagnoseRequest): Response<DiagnoseResponse>
    
    @GET("expert-system/questions")
    suspend fun getAllQuestions(): Response<List<Question>>
    
    // Transaction/Payment API
    @POST("payments/create")
    suspend fun createTransaction(@Body request: CreateTransactionRequest): Response<CreateTransactionResponse>
    
    @GET("payments/status/{orderId}")
    suspend fun getTransactionStatus(@Path("orderId") orderId: String): Response<TransactionStatus>
    
    @GET("payments/transaction/{orderId}")
    suspend fun getTransactionByOrderId(@Path("orderId") orderId: String): Response<Transaction>
    
    @POST("payments/cancel/{orderId}")
    suspend fun cancelTransaction(@Path("orderId") orderId: String): Response<Map<String, String>>
    
    @GET("payments/transactions")
    suspend fun getAllTransactions(): Response<List<Transaction>>
}
