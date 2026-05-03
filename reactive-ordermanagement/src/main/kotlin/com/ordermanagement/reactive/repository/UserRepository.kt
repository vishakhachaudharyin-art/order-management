package com.ordermanagement.reactive.repository

import com.ordermanagement.reactive.model.User
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface UserRepository : ReactiveMongoRepository<User, String>