package ru.pavlentygood.cellcapture.game.persistence

import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.autoconfigure.jdbc.TestDatabaseAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.jdbc.datasource.SingleConnectionDataSource
import org.testcontainers.containers.PostgreSQLContainer
import ru.pavlentygood.cellcapture.game.domain.RestoreParty

@Configuration
@ImportAutoConfiguration(exclude = [TestDatabaseAutoConfiguration::class])
@Import(value = [SavePartyToDatabase::class, GetPartyByPlayerFromDatabase::class, RestoreParty::class])
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
