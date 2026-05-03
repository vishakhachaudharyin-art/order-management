package com.ordermanagement.reactive.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class ProductRequestDTO(
    @field:NotBlank(message = "Product name is required")
    val name: String,

    @field:NotNull(message = "Price is mandatory")
    @field:Positive(message = "Price must be greater than 0")
    val price: Double,

    @field:NotNull(message = "Quantity is mandatory")
    @field:Positive(message = "Quantity must be greater than 0")
    val quantity: Int
)