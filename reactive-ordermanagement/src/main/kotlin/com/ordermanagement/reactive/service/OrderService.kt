package com.ordermanagement.reactive.service

import com.ordermanagement.reactive.dto.OrderRequestDTO
import com.ordermanagement.reactive.dto.OrderResponseDTO
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface OrderService {

    fun createOrder(userId: String , request: OrderRequestDTO): Mono<OrderResponseDTO>

    fun getOrder(id: String) : Mono<OrderResponseDTO>

    fun getAllOrders() : Flux<OrderResponseDTO>

    fun updateStatus(orderId: String , status : String) : Mono<OrderResponseDTO>

    fun updateOrder(orderId : String , request: OrderRequestDTO) : Mono<OrderResponseDTO>

    fun deleteOrder(id: String) : Mono<Void>

    fun deleteAllOrders() : Mono<Void>

}