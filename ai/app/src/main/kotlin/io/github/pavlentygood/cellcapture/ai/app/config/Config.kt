package io.github.pavlentygood.cellcapture.ai.app.config

import io.github.pavlentygood.cellcapture.ai.app.output.db.GetForestFromDatabase
import io.github.pavlentygood.cellcapture.ai.app.output.db.SaveForestToDatabase
import io.github.pavlentygood.cellcapture.ai.app.usecase.CreateForestUseCase
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@EnableScheduling
@ComponentScan("io.github.pavlentygood.cellcapture.ai.app.input.rest")
@EnableJdbcRepositories("io.github.pavlentygood.cellcapture.ai.app.output.db")
@Import(
    value = [
        CreateForestUseCase::class,
        SaveForestToDatabase::class,
        GetForestFromDatabase::class
    ]
)
class Config
