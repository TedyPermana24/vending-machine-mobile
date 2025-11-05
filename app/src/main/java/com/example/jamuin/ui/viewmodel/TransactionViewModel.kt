package com.example.jamuin.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jamuin.data.api.RetrofitInstance
import com.example.jamuin.data.model.*
import com.example.jamuin.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class TransactionUiState {
    object Initial : TransactionUiState()
    object Loading : TransactionUiState()
    data class Success(val response: CreateTransactionResponse) : TransactionUiState()
    data class Error(val message: String) : TransactionUiState()
}

sealed class TransactionStatusUiState {
    object Initial : TransactionStatusUiState()
    object Loading : TransactionStatusUiState()
    data class Success(val status: TransactionStatus) : TransactionStatusUiState()
    data class Error(val message: String) : TransactionStatusUiState()
}

class TransactionViewModel : ViewModel() {
    private val repository = TransactionRepository(RetrofitInstance.apiService)
    
    private val _uiState = MutableStateFlow<TransactionUiState>(TransactionUiState.Initial)
    val uiState: StateFlow<TransactionUiState> = _uiState.asStateFlow()
    
    private val _statusUiState = MutableStateFlow<TransactionStatusUiState>(TransactionStatusUiState.Initial)
    val statusUiState: StateFlow<TransactionStatusUiState> = _statusUiState.asStateFlow()
    
    fun createTransaction(
        productId: Int,
        quantity: Int,
        customerName: String
    ) {
        viewModelScope.launch {
            _uiState.value = TransactionUiState.Loading
            
            val request = CreateTransactionRequest(
                productId = productId,
                quantity = quantity,
                customerName = "User Mobile",
                platform = "mobile"
            )
            
            repository.createTransaction(request)
                .onSuccess { response ->
                    _uiState.value = TransactionUiState.Success(response)
                }
                .onFailure { error ->
                    _uiState.value = TransactionUiState.Error(
                        error.message ?: "Failed to create transaction"
                    )
                }
        }
    }
    
    fun getTransactionStatus(orderId: String) {
        viewModelScope.launch {
            _statusUiState.value = TransactionStatusUiState.Loading
            
            repository.getTransactionStatus(orderId)
                .onSuccess { status ->
                    _statusUiState.value = TransactionStatusUiState.Success(status)
                }
                .onFailure { error ->
                    _statusUiState.value = TransactionStatusUiState.Error(
                        error.message ?: "Failed to fetch transaction status"
                    )
                }
        }
    }
    
    fun cancelTransaction(orderId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.cancelTransaction(orderId)
                .onSuccess {
                    onSuccess()
                }
                .onFailure { error ->
                    _uiState.value = TransactionUiState.Error(
                        error.message ?: "Failed to cancel transaction"
                    )
                }
        }
    }
    
    fun resetState() {
        _uiState.value = TransactionUiState.Initial
        _statusUiState.value = TransactionStatusUiState.Initial
    }
}
