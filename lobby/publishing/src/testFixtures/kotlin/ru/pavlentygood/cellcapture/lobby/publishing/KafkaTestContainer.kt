package ru.pavlentygood.cellcapture.lobby.publishing

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.kafka.KafkaContainer

@Testcontainers
open class KafkaTestContainer {

    companion object {

        @JvmStatic
        @Container
        val kafkaContainer = KafkaContainer("apache/kafka:3.8.1")

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.kafka.bootstrap-servers") { kafkaContainer.bootstrapServers }
        }
    }
}
