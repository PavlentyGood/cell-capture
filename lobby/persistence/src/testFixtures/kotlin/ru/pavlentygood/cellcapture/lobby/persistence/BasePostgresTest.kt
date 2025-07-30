package ru.pavlentygood.cellcapture.lobby.persistence

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer

interface BasePostgresTest {

    companion object {

        private val container = PostgreSQLContainer<Nothing>("postgres:17.2-alpine")

        init {
            container.start()
        }

        @JvmStatic
        @DynamicPropertySource
        fun postgresProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") { container.jdbcUrl }
            registry.add("spring.datasource.username") { container.username }
            registry.add("spring.datasource.password") { container.password }
        }
    }
}