package com.ordermanagement.reactive.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank


data class UserRequestDTO(


    @field:NotBlank(message = "Name is mandatory")
    val name: String?,

    @field:NotBlank(message = "Email is mandatory")
    @field:Email(message = "Invalid email format")
    val email: String?
)


