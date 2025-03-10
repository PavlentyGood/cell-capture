package ru.pavlentygood.cellcapture.lobby.app.component

import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import ru.pavlentygood.cellcapture.lobby.persistence.BasePostgresTest
import ru.pavlentygood.cellcapture.lobby.publishing.BaseKafkaTest

@Testcontainers
sealed class TestContainers : BasePostgresTest, BaseKafkaTest {

    companion object {

        @Container
        val postgresContainer = BasePostgresTest.container

        @Container
        val kafkaContainer = BaseKafkaTest.container
    }
}
