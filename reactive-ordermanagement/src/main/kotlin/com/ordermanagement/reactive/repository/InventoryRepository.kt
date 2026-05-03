package com.ordermanagement.reactive.repository

import com.ordermanagement.reactive.model.Inventory
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface InventoryRepository : ReactiveMongoRepository<Inventory, String> {
    fun findByProductId(productId: String): Mono<Inventory>
}