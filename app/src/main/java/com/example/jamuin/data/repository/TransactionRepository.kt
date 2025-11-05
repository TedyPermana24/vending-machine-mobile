package com.example.jamuin.data.repository

import com.example.jamuin.data.api.ApiService
import com.example.jamuin.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TransactionRepository(private val apiService: ApiService) {
    
    suspend fun createTransaction(request: CreateTransactionRequest): Result<CreateTransactionResponse> = 
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.createTransaction(request)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Failed to create transaction: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    
    suspend fun getTransactionStatus(orderId: String): Result<TransactionStatus> = 
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.getTransactionStatus(orderId)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Failed to fetch transaction status: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    
    suspend fun getTransactionByOrderId(orderId: String): Result<Transaction> = 
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.getTransactionByOrderId(orderId)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Failed to fetch transaction: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    
    suspend fun cancelTransaction(orderId: String): Result<Map<String, String>> = 
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.cancelTransaction(orderId)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Failed to cancel transaction: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    
    suspend fun getAllTransactions(): Result<List<Transaction>> = 
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.getAllTransactions()
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Failed to fetch transactions: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}
