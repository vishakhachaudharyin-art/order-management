package com.ordermanagement.reactive.controllers

import com.ordermanagement.reactive.dto.OrderRequestDTO
import com.ordermanagement.reactive.dto.OrderResponseDTO
import com.ordermanagement.reactive.service.OrderService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/orders")
class OrderController(private val orderService: OrderService) {

    @PostMapping("/create")
    fun createOrder(
        @RequestParam userId: String,
        @Valid @RequestBody request: OrderRequestDTO
    ): Mono<ResponseEntity<OrderResponseDTO>> {
        return orderService.createOrder(userId, request)
            .map { order ->
                ResponseEntity.status(HttpStatus.CREATED).body(order)
            }
    }

    @GetMapping("/{id}")
    fun getOrderById(@PathVariable id: String): Mono<ResponseEntity<OrderResponseDTO>> {
        return orderService.getOrder(id)
            .map { order ->
                ResponseEntity.status(HttpStatus.OK).body(order)
            }
    }

    @GetMapping
    fun getAllOrders(): Mono<ResponseEntity<List<OrderResponseDTO>>> {
        return orderService.getAllOrders()
            .collectList()
            .map { orders ->
                ResponseEntity.status(HttpStatus.OK).body(orders)
            }
    }

    @PutMapping("/{id}")
    fun updateOrder(
        @PathVariable id: String,
        @Valid @RequestBody request: OrderRequestDTO
    ): Mono<ResponseEntity<OrderResponseDTO>> {
        return orderService.updateOrder(id, request)
            .map { updatedOrder ->
                ResponseEntity.status(HttpStatus.OK).body(updatedOrder)
            }
    }

    @PutMapping("/{id}/status")
    fun updateStatus(
        @PathVariable id: String,
        @RequestParam status: String
    ): Mono<ResponseEntity<OrderResponseDTO>> {
        return orderService.updateStatus(id, status)
            .map { updatedOrder ->
                ResponseEntity.status(HttpStatus.OK).body(updatedOrder)
            }
    }

    @DeleteMapping("/{id}")
    fun deleteOrder(@PathVariable id: String): Mono<ResponseEntity<Void>> {
        return orderService.deleteOrder(id)
            .then(Mono.just(
                ResponseEntity.status(HttpStatus.NO_CONTENT).build()
            ))
    }

    @DeleteMapping
    fun deleteAllOrders(): Mono<ResponseEntity<Void>> {
        return orderService.deleteAllOrders()
            .then(Mono.just(
                ResponseEntity.status(HttpStatus.NO_CONTENT).build()
            ))
    }
}