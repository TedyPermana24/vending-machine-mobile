package com.example.jamuin.data.model

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("nama")
    val nama: String,
    
    @SerializedName("deskripsi")
    val deskripsi: String,
    
    @SerializedName("manfaat")
    val manfaat: String,
    
    @SerializedName("harga")
    val harga: Int,
    
    @SerializedName("stok")
    val stok: Int,
    
    @SerializedName("gambar")
    val gambar: String?,
    
    @SerializedName("createdAt")
    val createdAt: String? = null,
    
    @SerializedName("updatedAt")
    val updatedAt: String? = null
)
