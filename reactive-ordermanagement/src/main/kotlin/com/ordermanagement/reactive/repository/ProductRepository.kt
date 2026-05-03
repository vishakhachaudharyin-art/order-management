package com.ordermanagement.reactive.repository

import com.ordermanagement.reactive.model.Product
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface ProductRepository : ReactiveMongoRepository<Product, String>