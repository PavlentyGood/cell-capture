package io.github.pavlentygood.cellcapture.lobby.app.integration.config

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
            registry.add("KAFKA_BOOTSTRAP_SERVERS") { container.bootstrapServers }
        }
    }
}
