package ru.pavlentygood.cellcapture.game.listening

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.kafka.KafkaContainer

interface BaseKafkaTest {

    companion object {

        private val container = KafkaContainer("apache/kafka:3.8.1")

        init {
            container.start()
        }

        @JvmStatic
        @DynamicPropertySource
        fun kafkaProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.kafka.bootstrap-servers") { container.bootstrapServers }
        }
    }
}