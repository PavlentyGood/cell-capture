package ru.pavlentygood.cellcapture.lobby.persistence

import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.autoconfigure.jdbc.TestDatabaseAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.jdbc.datasource.SingleConnectionDataSource
import org.testcontainers.containers.PostgreSQLContainer

@Configuration
@ImportAutoConfiguration(exclude = [TestDatabaseAutoConfiguration::class])
@Import(value = [
    GeneratePlayerIdBySequence::class,
//    GetPartyByPlayerFromDatabase::class,
//    GetPartyFromDatabase::class,
//    SavePartyToDatabase::class
])
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
