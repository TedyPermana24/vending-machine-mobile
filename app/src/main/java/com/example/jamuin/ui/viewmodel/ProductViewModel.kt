package com.example.jamuin.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jamuin.data.api.RetrofitInstance
import com.example.jamuin.data.model.Product
import com.example.jamuin.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ProductUiState {
    object Loading : ProductUiState()
    data class Success(val products: List<Product>) : ProductUiState()
    data class Error(val message: String) : ProductUiState()
}

class ProductViewModel : ViewModel() {
    private val repository = ProductRepository(RetrofitInstance.apiService)
    
    private val _uiState = MutableStateFlow<ProductUiState>(ProductUiState.Loading)
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()
    
    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct.asStateFlow()
    
    init {
        loadProducts()
    }
    
    fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = ProductUiState.Loading
            repository.getAllProducts()
                .onSuccess { products ->
                    _uiState.value = ProductUiState.Success(products)
                }
                .onFailure { error ->
                    _uiState.value = ProductUiState.Error(
                        error.message ?: "Unknown error occurred"
                    )
                }
        }
    }
    
    fun loadProductById(id: Int) {
        viewModelScope.launch {
            repository.getProductById(id)
                .onSuccess { product ->
                    _selectedProduct.value = product
                }
                .onFailure { error ->
                    // Handle error if needed
                }
        }
    }
    
    fun setSelectedProduct(product: Product?) {
        _selectedProduct.value = product
    }
}
