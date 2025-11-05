package com.example.jamuin.data.model

import com.google.gson.annotations.SerializedName

data class Transaction(
    @SerializedName("id")
    val id: Int? = null,
    
    @SerializedName("orderId")
    val orderId: String,
    
    @SerializedName("productId")
    val productId: Int,
    
    @SerializedName("quantity")
    val quantity: Int,
    
    @SerializedName("grossAmount")
    val grossAmount: Int,
    
    @SerializedName("status")
    val status: String,
    
    @SerializedName("paymentType")
    val paymentType: String? = null,
    
    @SerializedName("snapToken")
    val snapToken: String? = null,
    
    @SerializedName("snapUrl")
    val snapUrl: String? = null,
    
    @SerializedName("transactionId")
    val transactionId: String? = null,
    
    @SerializedName("customerName")
    val customerName: String,
    
    @SerializedName("customerEmail")
    val customerEmail: String,
    
    @SerializedName("customerPhone")
    val customerPhone: String,
    
    @SerializedName("platform")
    val platform: String = "mobile",
    
    @SerializedName("paidAt")
    val paidAt: String? = null,
    
    @SerializedName("createdAt")
    val createdAt: String? = null,
    
    @SerializedName("updatedAt")
    val updatedAt: String? = null,
    
    @SerializedName("product")
    val product: Product? = null
)

data class CreateTransactionRequest(
    @SerializedName("productId")
    val productId: Int,
    
    @SerializedName("quantity")
    val quantity: Int,
    
    @SerializedName("customerName")
    val customerName: String,
    
    @SerializedName("platform")
    val platform: String = "mobile"
)

data class CreateTransactionResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("orderId")
    val orderId: String,
    
    @SerializedName("snapToken")
    val snapToken: String,
    
    @SerializedName("snapUrl")
    val snapUrl: String,
    
    @SerializedName("grossAmount")
    val grossAmount: Int,
    
    @SerializedName("product")
    val product: Product,
    
    @SerializedName("message")
    val message: String
)

data class TransactionStatus(
    @SerializedName("orderId")
    val orderId: String,
    
    @SerializedName("status")
    val status: String,
    
    @SerializedName("transactionId")
    val transactionId: String?,
    
    @SerializedName("paymentType")
    val paymentType: String?,
    
    @SerializedName("grossAmount")
    val grossAmount: Int,
    
    @SerializedName("paidAt")
    val paidAt: String?,
    
    @SerializedName("product")
    val product: Product?,
    
    @SerializedName("customer")
    val customer: Customer?
)

data class Customer(
    @SerializedName("name")
    val name: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("phone")
    val phone: String
)
