package com.ordermanagement.reactive.controllers

import com.ordermanagement.reactive.dto.UserRequestDTO
import com.ordermanagement.reactive.dto.UserResponseDTO
import com.ordermanagement.reactive.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @PostMapping("/create")
    fun createUser(
        @Valid @RequestBody dto: UserRequestDTO
    ): Mono<ResponseEntity<UserResponseDTO>> {
        return userService.createUser(dto)
            .map { user ->
                ResponseEntity.status(HttpStatus.CREATED).body(user)
            }
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: String): Mono<ResponseEntity<UserResponseDTO>> {
        return userService.getUserById(id)
            .map { user ->
                ResponseEntity.status(HttpStatus.OK).body(user)
            }
    }

    @GetMapping
    fun getAllUsers(): Mono<ResponseEntity<List<UserResponseDTO>>> {
        return userService.getAllUsers()
            .collectList()
            .map { users ->
                ResponseEntity.status(HttpStatus.OK).body(users)
            }
    }

    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: String,
        @Valid @RequestBody dto: UserRequestDTO
    ): Mono<ResponseEntity<UserResponseDTO>> {
        return userService.updateUser(id, dto)
            .map { updatedUser ->
                ResponseEntity.status(HttpStatus.OK).body(updatedUser)
            }
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: String): Mono<ResponseEntity<Void>> {
        return userService.deleteUser(id)
            .then(Mono.just(
                ResponseEntity.status(HttpStatus.NO_CONTENT).build()
            ))
    }

    @DeleteMapping("/deleteAll")
    fun deleteAllUsers(): Mono<ResponseEntity<Void>> {
        return userService.deleteAllUsers()
            .then(Mono.just(
                ResponseEntity.status(HttpStatus.NO_CONTENT).build()
            ))
    }
}