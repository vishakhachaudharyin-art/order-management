package com.ordermanagement.reactive.service.imp

import com.ordermanagement.reactive.dto.OrderRequestDTO
import com.ordermanagement.reactive.dto.OrderResponseDTO
import com.ordermanagement.reactive.events.OutOfStockEvent
import com.ordermanagement.reactive.exception.BadRequestException
import com.ordermanagement.reactive.exception.ResourceNotFoundException
import com.ordermanagement.reactive.kafka.KafkaProducer
import com.ordermanagement.reactive.mapper.Mapper
import com.ordermanagement.reactive.model.*
import com.ordermanagement.reactive.repository.*
import com.ordermanagement.reactive.service.OrderService
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class OrderServiceImp(
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository,
    private val inventoryRepository: InventoryRepository,
    private val waitingUserRepository: WaitingUserRepository,
    private val kafkaProducer: KafkaProducer
) : OrderService {

    override fun createOrder(userId: String, request: OrderRequestDTO): Mono<OrderResponseDTO> {

        if (request.productIds.size != request.quantities.size) {
            return Mono.error(
                BadRequestException(
                    "Validation failed",
                    mapOf("request" to "Product IDs and quantities must match")
                )
            )
        }

        return userRepository.findById(userId)
            .switchIfEmpty(
                Mono.error(
                    ResourceNotFoundException(
                        "Validation failed",
                        mapOf("userId" to "User not found")
                    )
                )
            )
            .flatMap { user ->

                Flux.fromIterable(request.productIds.zip(request.quantities))
                    .flatMap { (productId, qty) ->

                        inventoryRepository.findByProductId(productId)
                            .switchIfEmpty(
                                Mono.error(
                                    ResourceNotFoundException(
                                        "Validation failed",
                                        mapOf("productId" to "Product not found in inventory")
                                    )
                                )
                            )
                            .flatMap { inventory ->

                                if (inventory.quantity < qty) {

                                    return@flatMap waitingUserRepository.save(
                                        WaitingUser(
                                            productId = productId,
                                            userId = userId
                                        )
                                    )
                                        .doOnSuccess {
                                            kafkaProducer.sendOutOfStock(
                                                OutOfStockEvent(productId, userId, qty)
                                            )
                                        }
                                        .then(
                                            Mono.error(
                                                BadRequestException(
                                                    "Validation failed",
                                                    mapOf("stock" to "Product out of stock")
                                                )
                                            )
                                        )
                                }

                                inventory.quantity -= qty

                                inventoryRepository.save(inventory)
                                    .then(productRepository.findById(productId))
                                    .map {
                                        OrderItem(it.id!!, qty, it.price)
                                    }
                            }
                    }
                    .collectList()
                    .flatMap { items ->

                        val total = items.sumOf { it.price * it.quantity }

                        val order = Order(
                            userId = user.id!!,
                            items = items,
                            totalAmount = total,
                            status = OrderStatus.CREATED
                        )

                        orderRepository.save(order)
                    }
            }
            .map { Mapper.toOrderResponse(it) }
    }

    override fun getOrder(id: String): Mono<OrderResponseDTO> {
        return orderRepository.findById(id)
            .switchIfEmpty(
                Mono.error(
                    ResourceNotFoundException(
                        "Validation failed",
                        mapOf("orderId" to "Order not found")
                    )
                )
            )
            .map { Mapper.toOrderResponse(it) }
    }

    override fun getAllOrders(): Flux<OrderResponseDTO> {
        return orderRepository.findAll()
            .map { Mapper.toOrderResponse(it) }
    }

    override fun updateOrder(orderId: String, request: OrderRequestDTO): Mono<OrderResponseDTO> {

        if (request.productIds.size != request.quantities.size) {
            return Mono.error(
                BadRequestException(
                    "Validation failed",
                    mapOf("request" to "Mismatch between products and quantities")
                )
            )
        }

        return orderRepository.findById(orderId)
            .switchIfEmpty(
                Mono.error(
                    ResourceNotFoundException(
                        "Validation failed",
                        mapOf("orderId" to "Order not found")
                    )
                )
            )
            .flatMap { existingOrder ->

                Flux.fromIterable(request.productIds.zip(request.quantities))
                    .flatMap { (productId, qty) ->

                        inventoryRepository.findByProductId(productId)
                            .switchIfEmpty(
                                Mono.error(
                                    ResourceNotFoundException(
                                        "Validation failed",
                                        mapOf("productId" to "Product not found in inventory")
                                    )
                                )
                            )
                            .flatMap { inventory ->

                                if (inventory.quantity < qty) {

                                    return@flatMap waitingUserRepository.save(
                                        WaitingUser(
                                            productId = productId,
                                            userId = existingOrder.userId
                                        ))

                                        .doOnSuccess {
                                            kafkaProducer.sendOutOfStock(
                                                OutOfStockEvent(
                                                    productId,
                                                    existingOrder.userId,
                                                    qty
                                                )
                                            )
                                        }
                                        .then(
                                            Mono.error(
                                                BadRequestException(
                                                    "Validation failed",
                                                    mapOf("stock" to "Product out of stock")
                                                )
                                            )
                                        )
                                }

                                inventory.quantity -= qty

                                inventoryRepository.save(inventory)
                                    .then(productRepository.findById(productId))
                                    .map {
                                        OrderItem(it.id!!, qty, it.price)
                                    }
                            }
                    }
                    .collectList()
                    .flatMap { items ->

                        existingOrder.items = items
                        existingOrder.totalAmount = items.sumOf { it.price * it.quantity }

                        orderRepository.save(existingOrder)
                    }
            }
            .map { Mapper.toOrderResponse(it) }
    }

    override fun updateStatus(orderId: String, status: String): Mono<OrderResponseDTO> {
        return orderRepository.findById(orderId)
            .switchIfEmpty(
                Mono.error(
                    ResourceNotFoundException(
                        "Validation failed",
                        mapOf("orderId" to "Order not found")
                    )
                )
            )
            .flatMap {
                val orderStatus = try {
                    OrderStatus.valueOf(status.uppercase())
                } catch (ex: Exception) {
                    throw BadRequestException(
                        "Validation failed",
                        mapOf("status" to "Invalid order status")
                    )
                }

                it.status = orderStatus
                orderRepository.save(it)
            }
            .map { Mapper.toOrderResponse(it) }
    }

    override fun deleteOrder(id: String): Mono<Void> {
        return orderRepository.findById(id)
            .switchIfEmpty(
                Mono.error(
                    ResourceNotFoundException(
                        "Validation failed",
                        mapOf("orderId" to "Order not found")
                    )
                )
            )
            .flatMap { orderRepository.delete(it) }
    }

    override fun deleteAllOrders(): Mono<Void> {
        return orderRepository.deleteAll()
    }
}