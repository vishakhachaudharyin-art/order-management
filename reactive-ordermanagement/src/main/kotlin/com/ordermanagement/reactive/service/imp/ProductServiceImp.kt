package com.ordermanagement.reactive.service.imp

import com.ordermanagement.reactive.dto.ProductRequestDTO
import com.ordermanagement.reactive.dto.ProductResponseDTO
import com.ordermanagement.reactive.exception.ResourceNotFoundException
import com.ordermanagement.reactive.mapper.Mapper
import com.ordermanagement.reactive.model.Inventory
import com.ordermanagement.reactive.repository.InventoryRepository
import com.ordermanagement.reactive.repository.ProductRepository
import com.ordermanagement.reactive.service.ProductService
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ProductServiceImp(
    private val productRepository: ProductRepository,
    private val inventoryRepository: InventoryRepository
) : ProductService {

    override fun createProduct(dto: ProductRequestDTO): Mono<ProductResponseDTO> {

        return productRepository.save(Mapper.toProductEntity(dto))
            .flatMap { savedProduct ->

                val inventory = Inventory(
                    productId = savedProduct.id!!,
                    quantity = dto.quantity
                )

                inventoryRepository.save(inventory)
                    .thenReturn(savedProduct)
            }
            .map { Mapper.toProductResponse(it) }
    }

    override fun getProduct(id: String): Mono<ProductResponseDTO> {
        return productRepository.findById(id)
            .switchIfEmpty(
                Mono.error(
                    ResourceNotFoundException(
                        "Validation failed",
                        mapOf("productId" to "Product not found")
                    )
                )
            )
            .map { Mapper.toProductResponse(it) }
    }

    override fun getAllProducts(): Flux<ProductResponseDTO> {
        return productRepository.findAll()
            .map { Mapper.toProductResponse(it) }
    }

    override fun updateProduct(id: String, dto: ProductRequestDTO): Mono<ProductResponseDTO> {
        return productRepository.findById(id)
            .switchIfEmpty(
                Mono.error(
                    ResourceNotFoundException(
                        "Validation failed",
                        mapOf("productId" to "Product not found")
                    )
                )
            )
            .flatMap {
                val updated = it.copy(name = dto.name, price = dto.price)
                productRepository.save(updated)
            }
            .map { Mapper.toProductResponse(it) }
    }

    override fun deleteProduct(id: String): Mono<Void> {
        return productRepository.findById(id)
            .switchIfEmpty(
                Mono.error(
                    ResourceNotFoundException(
                        "Validation failed",
                        mapOf("productId" to "Product not found")
                    )
                )
            )
            .flatMap { productRepository.delete(it) }
    }

    override fun deleteAllProducts(): Mono<Void> {
        return productRepository.deleteAll()
    }
}