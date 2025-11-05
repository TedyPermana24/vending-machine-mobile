package com.example.jamuin.data.model

data class CartItem(
    val product: Product,
    val quantity: Int
) {
    val totalPrice: Int
        get() = product.harga * quantity
}
