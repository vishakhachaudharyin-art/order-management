package com.ordermanagement.reactive.exception

class ResourceNotFoundException(
    message: String,
    val fieldErrors: Map<String, String>? = null
) : RuntimeException(message)