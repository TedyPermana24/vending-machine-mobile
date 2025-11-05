package com.example.jamuin.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.jamuin.data.model.CartItem
import com.example.jamuin.data.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CartViewModel : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()
    
    val totalPrice: StateFlow<Int> = MutableStateFlow(0).apply {
        _cartItems.value.forEach { item ->
            value += item.totalPrice
        }
    }
    
    fun addToCart(product: Product, quantity: Int = 1) {
        val currentItems = _cartItems.value.toMutableList()
        val existingItemIndex = currentItems.indexOfFirst { it.product.id == product.id }
        
        if (existingItemIndex != -1) {
            // Update quantity if product already exists
            val existingItem = currentItems[existingItemIndex]
            currentItems[existingItemIndex] = existingItem.copy(
                quantity = existingItem.quantity + quantity
            )
        } else {
            // Add new item
            currentItems.add(CartItem(product, quantity))
        }
        
        _cartItems.value = currentItems
        updateTotalPrice()
    }
    
    fun removeFromCart(productId: Int) {
        _cartItems.value = _cartItems.value.filter { it.product.id != productId }
        updateTotalPrice()
    }
    
    fun updateQuantity(productId: Int, quantity: Int) {
        val currentItems = _cartItems.value.toMutableList()
        val itemIndex = currentItems.indexOfFirst { it.product.id == productId }
        
        if (itemIndex != -1) {
            if (quantity > 0) {
                currentItems[itemIndex] = currentItems[itemIndex].copy(quantity = quantity)
            } else {
                currentItems.removeAt(itemIndex)
            }
            _cartItems.value = currentItems
            updateTotalPrice()
        }
    }
    
    fun clearCart() {
        _cartItems.value = emptyList()
        updateTotalPrice()
    }
    
    private fun updateTotalPrice() {
        val total = _cartItems.value.sumOf { it.totalPrice }
        (totalPrice as MutableStateFlow).value = total
    }
    
    fun getTotalItems(): Int {
        return _cartItems.value.sumOf { it.quantity }
    }
    
    fun getTotalPrice(): Int {
        return _cartItems.value.sumOf { it.totalPrice }
    }
}
