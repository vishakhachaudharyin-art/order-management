package com.ordermanagement.reactive.dto

import com.ordermanagement.reactive.model.OrderStatus

data class OrderResponseDTO(

    val id: String,
    val userId: String,
    val items: List<OrderItemDTO>,
    val totalAmount: Double,
    val status: OrderStatus
)
