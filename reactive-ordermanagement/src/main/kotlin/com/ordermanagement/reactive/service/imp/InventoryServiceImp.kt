package com.ordermanagement.reactive.service.imp

import com.ordermanagement.reactive.events.RestockEvent
import com.ordermanagement.reactive.kafka.KafkaProducer
import com.ordermanagement.reactive.model.Inventory
import com.ordermanagement.reactive.repository.InventoryRepository
import com.ordermanagement.reactive.service.InventoryService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class InventoryServiceImp(
    private val inventoryRepository: InventoryRepository,
    private val kafkaProducer: KafkaProducer
) : InventoryService {

    override fun restock(productId: String, quantity: Int): Mono<Void> {

        return inventoryRepository.findByProductId(productId)
            .switchIfEmpty(
                Mono.error(RuntimeException("Inventory not found for productId=$productId"))
            )
            .flatMap { inventory ->

                println("BEFORE RESTOCK -> ${inventory.quantity}")

                inventory.quantity += quantity

                println("AFTER RESTOCK -> ${inventory.quantity}")
                println("AFTER RESTOCK PJ -> ${inventory.quantity}")


                inventoryRepository.save(inventory)
            }
            .doOnSuccess {
                println("KAFKA -> RestockEvent sending...")
                kafkaProducer.sendRestock(
                    RestockEvent(productId, quantity)
                )
            }
            .then()
    }

}