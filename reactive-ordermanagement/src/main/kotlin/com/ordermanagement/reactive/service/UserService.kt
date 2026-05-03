package com.ordermanagement.reactive.service

import com.ordermanagement.reactive.dto.ProductRequestDTO
import com.ordermanagement.reactive.dto.ProductResponseDTO
import com.ordermanagement.reactive.dto.UserRequestDTO
import com.ordermanagement.reactive.dto.UserResponseDTO
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface UserService {

    fun createUser(dto: UserRequestDTO) : Mono<UserResponseDTO>

    fun getAllUsers() : Flux<UserResponseDTO>

    fun getUserById( id: String) : Mono<UserResponseDTO>

    fun updateUser(id : String , dto: UserRequestDTO) : Mono<UserResponseDTO>

    fun deleteUser( id : String) : Mono<Void>

    fun deleteAllUsers(): Mono<Void>
}