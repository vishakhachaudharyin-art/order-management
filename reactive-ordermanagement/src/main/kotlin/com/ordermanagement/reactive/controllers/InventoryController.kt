package com.ordermanagement.reactive.controllers

import com.ordermanagement.reactive.service.InventoryService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/inventory")
class InventoryController(
    private val inventoryService: InventoryService
) {

    @PostMapping("/{productId}/restock")
    fun restock(
        @PathVariable productId: String,
        @RequestParam quantity: Int
    ): Mono<String> {
        return inventoryService.restock(productId, quantity)
            .thenReturn("Restocked successfully")
    }
}