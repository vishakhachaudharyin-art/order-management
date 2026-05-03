package com.ordermanagement.reactive.service

import reactor.core.publisher.Mono

interface InventoryService {
    fun restock(productId: String, quantity: Int): Mono<Void>
}