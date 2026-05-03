package com.ordermanagement.reactive.service

import com.ordermanagement.reactive.dto.ProductRequestDTO
import com.ordermanagement.reactive.dto.ProductResponseDTO
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ProductService {
    fun createProduct(dto: ProductRequestDTO) : Mono<ProductResponseDTO>

    fun getProduct(id: String) : Mono<ProductResponseDTO>

    fun getAllProducts(): Flux<ProductResponseDTO>

    fun updateProduct(id : String , dto: ProductRequestDTO ) : Mono<ProductResponseDTO>

    fun deleteProduct(id: String) : Mono<Void>

    fun deleteAllProducts() : Mono<Void>
}