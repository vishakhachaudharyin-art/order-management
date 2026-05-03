package com.ordermanagement.reactive.kafka

import com.ordermanagement.reactive.events.RestockEvent
import com.ordermanagement.reactive.repository.WaitingUserRepository
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class KafkaConsumer(
    private val waitingUserRepository: WaitingUserRepository
) {

    private val log = LoggerFactory.getLogger(KafkaConsumer::class.java)

    @KafkaListener(topics = ["restock-topic"], groupId = "order-group")
    fun consumeRestock(event: RestockEvent) {

        log.warn(
            "KAFKA CONSUMER HIT -> RestockEvent RECEIVED | productId={} qty={}",
            event.productId, event.quantity
        )

        waitingUserRepository.findByProductId(event.productId)
            .doOnNext {
                log.info(
                    "Notify user {}: Product {} is now available",
                    it.userId, event.productId
                )
            }
            .thenMany(
                waitingUserRepository.deleteByProductId(event.productId)
            )
            .doOnComplete {
                log.info("Waiting users cleared for productId={}", event.productId)
            }
            .subscribe()
    }
}