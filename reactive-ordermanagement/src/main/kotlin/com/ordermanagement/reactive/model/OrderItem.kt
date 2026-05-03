package com.ordermanagement.reactive.model

data class OrderItem(
    val productId: String,
    val quantity: Int,
    val price: Double
)
