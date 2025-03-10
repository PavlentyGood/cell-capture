package ru.pavlentygood.cellcapture.lobby.publishing

import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
sealed class KafkaTestContainer : BaseKafkaTest {

    companion object {

        @Container
        val kafkaContainer = BaseKafkaTest.container
    }
}
