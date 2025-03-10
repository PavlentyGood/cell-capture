package ru.pavlentygood.cellcapture.lobby.publishing

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.kafka.KafkaContainer

interface BaseKafkaTest {

    companion object {

        val container = KafkaContainer("apache/kafka:3.8.1")

        @JvmStatic
        @DynamicPropertySource
        fun kafkaProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.kafka.bootstrap-servers") { container.bootstrapServers }
        }
    }
}