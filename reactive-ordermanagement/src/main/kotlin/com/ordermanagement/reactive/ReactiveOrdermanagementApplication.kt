package com.ordermanagement.reactive

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ReactiveOrdermanagementApplication

fun main(args: Array<String>) {
	runApplication<ReactiveOrdermanagementApplication>(*args)
}
