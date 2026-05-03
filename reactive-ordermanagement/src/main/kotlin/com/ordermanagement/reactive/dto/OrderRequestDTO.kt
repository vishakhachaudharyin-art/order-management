package com.ordermanagement.reactive.dto

import jakarta.validation.constraints.NotEmpty

data class OrderRequestDTO(
    @field:NotEmpty(message = "Product list cannot be empty")
    val productIds: List<String>,

    @field:NotEmpty( message = "Quantity list cannot be empty")
    val quantities: List<Int>
)
