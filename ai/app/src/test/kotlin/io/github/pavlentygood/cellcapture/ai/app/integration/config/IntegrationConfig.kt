package io.github.pavlentygood.cellcapture.ai.app.integration.config

import io.github.pavlentygood.cellcapture.ai.app.output.db.GetForestFromDatabase
import io.github.pavlentygood.cellcapture.ai.app.output.db.SaveForestToDatabase
import io.github.pavlentygood.cellcapture.ai.app.usecase.CreateForestUseCase
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Import
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories

@TestConfiguration
@EnableAutoConfiguration
@EnableJdbcRepositories("io.github.pavlentygood.cellcapture.ai.app.output.db")
@Import(
    value = [
        CreateForestUseCase::class,
        SaveForestToDatabase::class,
        GetForestFromDatabase::class
    ]
)
class IntegrationConfig
