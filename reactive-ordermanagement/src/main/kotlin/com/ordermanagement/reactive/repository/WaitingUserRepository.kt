package com.ordermanagement.reactive.repository

import com.ordermanagement.reactive.model.WaitingUser
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux

interface WaitingUserRepository : ReactiveMongoRepository<WaitingUser, String> {
    fun findByProductId(productId: String): Flux<WaitingUser>
    fun deleteByProductId(productId: String): Flux<WaitingUser>
}