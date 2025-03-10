package ru.pavlentygood.cellcapture.lobby.persistence

import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
sealed class PostgresTestContainer : BasePostgresTest {

    companion object {

        @Container
        val postgresContainer = BasePostgresTest.container
    }
}
