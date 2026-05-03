package com.ordermanagement.reactive.exception

import org.springframework.core.codec.DecodingException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.server.ServerWebInputException
import reactor.core.publisher.Mono

data class ErrorResponse(
    val message: String,
    val fieldErrors: Map<String, String>? = null
)

@RestControllerAdvice
class GlobalExceptionHandler {

    //  VALIDATION ERROR
    @ExceptionHandler(WebExchangeBindException::class)
    fun handleValidationException(
        ex: WebExchangeBindException
    ): Mono<ResponseEntity<ErrorResponse>> {

        val errors = ex.fieldErrors.associate {
            it.field to (it.defaultMessage ?: "Invalid value")
        }
        val response = ErrorResponse(
            message = "Validation failed",
            fieldErrors = errors
        )
        return Mono.just(
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
        )
    }


    // RESOURCE NOT FOUND
    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleNotFound(
        ex: ResourceNotFoundException
    ): Mono<ResponseEntity<ErrorResponse>> {

        val response = ErrorResponse(
            message = ex.message ?: "Resource not found",
            fieldErrors = ex.fieldErrors
        )

        return Mono.just(
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
        )
    }

    //  BAD REQUEST
    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequest(
        ex: BadRequestException
    ): Mono<ResponseEntity<ErrorResponse>> {

        val response = ErrorResponse(
            message = ex.message ?: "Bad request",
            fieldErrors = ex.fieldErrors
        )

        return Mono.just(
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
        )
    }

    // GENERAL EXCEPTION
    @ExceptionHandler(Exception::class)
    fun handleGeneral(ex: Exception): Mono<ResponseEntity<ErrorResponse>> {

        val response = ErrorResponse(
            message = "Internal server error",
            fieldErrors = mapOf("error" to (ex.message ?: "Unexpected error"))
        )

        return Mono.just(
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response)
        )
    }
}