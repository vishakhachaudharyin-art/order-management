package com.ordermanagement.reactive.dto

import org.springframework.data.annotation.Id

data class OrderItemDTO(
    val productId: String,
    val quantity:Int,
    val price: Double
)
