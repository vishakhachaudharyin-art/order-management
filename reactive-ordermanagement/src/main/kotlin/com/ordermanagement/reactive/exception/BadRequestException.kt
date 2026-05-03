package com.ordermanagement.reactive.exception

class BadRequestException(
    message: String,
    val fieldErrors: Map<String, String>? = null
) : RuntimeException(message)