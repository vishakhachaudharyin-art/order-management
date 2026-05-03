package com.ordermanagement.reactive.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("inventory")
data class Inventory(
    @Id
    val id: String? = null,
    val productId: String,
    var quantity: Int
)