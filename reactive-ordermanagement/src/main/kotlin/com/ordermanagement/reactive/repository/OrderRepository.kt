package com.ordermanagement.reactive.repository

import com.ordermanagement.reactive.model.Order
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface OrderRepository : ReactiveMongoRepository<Order, String>