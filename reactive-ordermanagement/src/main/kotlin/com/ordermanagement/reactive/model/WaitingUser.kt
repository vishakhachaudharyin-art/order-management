package com.ordermanagement.reactive.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("waiting_users")
data class WaitingUser(
    @Id
    val id: String? = null,
    val productId: String,
    val userId: String
)