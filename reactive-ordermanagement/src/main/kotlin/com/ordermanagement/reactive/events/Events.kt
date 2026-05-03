package com.ordermanagement.reactive.events


data class OutOfStockEvent(
    val productId: String,
    val userId: String,
    val quantity: Int
)

data class RestockEvent(
    val productId: String,
    val quantity: Int
)