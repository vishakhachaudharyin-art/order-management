package com.ordermanagement.reactive.service.imp

import com.ordermanagement.reactive.dto.UserRequestDTO
import com.ordermanagement.reactive.dto.UserResponseDTO
import com.ordermanagement.reactive.exception.ResourceNotFoundException
import com.ordermanagement.reactive.mapper.Mapper
import com.ordermanagement.reactive.repository.UserRepository
import com.ordermanagement.reactive.service.UserService
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class UserServiceImp(
    private val userRepository: UserRepository
) : UserService {

    override fun createUser(dto: UserRequestDTO): Mono<UserResponseDTO> {
        return userRepository.save(Mapper.toUserEntity(dto))
            .map { Mapper.toUserResponse(it) }
    }

    override fun getAllUsers(): Flux<UserResponseDTO> {
        return userRepository.findAll()
            .map { Mapper.toUserResponse(it) }
    }

    override fun getUserById(id: String): Mono<UserResponseDTO> {
        return userRepository.findById(id)
            .switchIfEmpty(Mono.error(
                ResourceNotFoundException("Validation failed", mapOf("userId" to "User not found"))
            ))
            .map { Mapper.toUserResponse(it) }
    }

    override fun updateUser(id: String, dto: UserRequestDTO): Mono<UserResponseDTO> {
        return userRepository.findById(id)
            .switchIfEmpty(Mono.error(
                ResourceNotFoundException("Validation failed", mapOf("userId" to "User not found"))
            ))
            .flatMap {
                val updated = it.copy(name = dto.name, email = dto.email)
                userRepository.save(updated)
            }
            .map { Mapper.toUserResponse(it) }
    }

    override fun deleteUser(id: String): Mono<Void> {
        return userRepository.findById(id)
            .switchIfEmpty(Mono.error(
                ResourceNotFoundException("Validation failed", mapOf("userId" to "User not found"))
            ))
            .flatMap { userRepository.delete(it) }
    }

    override fun deleteAllUsers(): Mono<Void> {
        return userRepository.deleteAll()
    }
}