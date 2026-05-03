package com.ordermanagement.reactive.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "orders")
data class Order(
    @Id
    val id:String? = null,
    val userId: String,
    var items : List<OrderItem>,
    var totalAmount:Double,
    var status: OrderStatus = OrderStatus.CREATED
)
