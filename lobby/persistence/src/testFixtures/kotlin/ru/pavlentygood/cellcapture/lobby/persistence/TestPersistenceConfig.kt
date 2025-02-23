package ru.pavlentygood.cellcapture.lobby.persistence

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.SingleConnectionDataSource
import org.testcontainers.containers.PostgreSQLContainer

@Configuration
class TestPersistenceConfig {

    @Bean(initMethod = "start")
    fun postgresqlContainer() = PostgreSQLContainer<Nothing>("postgres:17.2-alpine")

    @Bean
    fun dataSource(container: PostgreSQLContainer<Nothing>) =
        SingleConnectionDataSource(
            container.jdbcUrl,
            container.username,
            container.password,
            true
        )
}
