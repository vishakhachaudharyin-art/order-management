package com.ordermanagement.reactive.controllers

import com.ordermanagement.reactive.dto.ProductRequestDTO
import com.ordermanagement.reactive.dto.ProductResponseDTO
import com.ordermanagement.reactive.service.ProductService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/products")
class ProductController(private val productService: ProductService) {

    @PostMapping("/create")
    fun createProduct(@Valid @RequestBody dto: ProductRequestDTO): Mono<ResponseEntity<ProductResponseDTO>> {
        return productService.createProduct(dto)
            .map { product ->
                ResponseEntity.status(HttpStatus.CREATED).body(product)
            }
    }

    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: String): Mono<ResponseEntity<ProductResponseDTO>> {
        return productService.getProduct(id)
            .map { product ->
                ResponseEntity.status(HttpStatus.OK).body(product)
            }
    }

    @GetMapping
    fun getAllProducts(): Mono<ResponseEntity<List<ProductResponseDTO>>> {
        return productService.getAllProducts()
            .collectList()
            .map { products ->
                ResponseEntity.status(HttpStatus.OK).body(products)
            }
    }

    @PutMapping("/{id}")
    fun updateProduct(
        @PathVariable id: String,
        @Valid @RequestBody dto: ProductRequestDTO
    ): Mono<ResponseEntity<ProductResponseDTO>> {
        return productService.updateProduct(id, dto)
            .map { updatedProduct ->
                ResponseEntity.status(HttpStatus.OK).body(updatedProduct)
            }
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: String): Mono<ResponseEntity<Void>> {
        return productService.deleteProduct(id)
            .then(Mono.just(
                ResponseEntity.status(HttpStatus.NO_CONTENT).build()
            ))
    }

    @DeleteMapping("/deleteAll")
    fun deleteAllProducts(): Mono<ResponseEntity<Void>> {
        return productService.deleteAllProducts()
            .then(Mono.just(
                ResponseEntity.status(HttpStatus.NO_CONTENT).build()
            ))
    }
}