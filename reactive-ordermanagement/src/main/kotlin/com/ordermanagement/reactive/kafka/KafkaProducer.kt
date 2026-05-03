package com.ordermanagement.reactive.kafka

import com.ordermanagement.reactive.events.OutOfStockEvent
import com.ordermanagement.reactive.events.RestockEvent
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class KafkaProducer(
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {

    private val log = LoggerFactory.getLogger(KafkaProducer::class.java)

    fun sendOutOfStock(event: OutOfStockEvent) {

        log.warn(
            "KAFKA PRODUCER HIT -> OutOfStockEvent SENT | productId={} userId={} qty={}",
            event.productId, event.userId, event.quantity
        )

        kafkaTemplate.send("out-of-stock-topic", event.productId, event)
    }

    fun sendRestock(event: RestockEvent) {

        log.info(
            "KAFKA PRODUCER HIT -> RestockEvent SENT | productId={} qty={}",
            event.productId, event.quantity
        )
        kafkaTemplate.send("restock-topic", event.productId, event)
    }
}