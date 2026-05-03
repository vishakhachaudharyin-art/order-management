package com.ordermanagement.reactive.config

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KafkaTopicConfig {

    @Bean
    fun outOfStockTopic() = NewTopic("out-of-stock-topic", 1, 1)

    @Bean
    fun restockTopic() = NewTopic("restock-topic", 1, 1)
}