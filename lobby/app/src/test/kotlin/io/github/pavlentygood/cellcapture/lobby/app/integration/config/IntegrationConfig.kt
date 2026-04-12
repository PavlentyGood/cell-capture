package io.github.pavlentygood.cellcapture.lobby.app.integration.config

import io.github.pavlentygood.cellcapture.lobby.domain.PartyFactory
import io.github.pavlentygood.cellcapture.lobby.app.output.db.GeneratePlayerIdBySequence
import io.github.pavlentygood.cellcapture.lobby.app.output.db.GetPartyByPlayerFromDatabase
import io.github.pavlentygood.cellcapture.lobby.app.output.db.GetPartyFromDatabase
import io.github.pavlentygood.cellcapture.lobby.app.output.db.SavePartyToDatabase
import io.github.pavlentygood.cellcapture.lobby.app.input.rest.ErrorHandler
import io.github.pavlentygood.cellcapture.lobby.app.usecase.CreatePartyUseCase
import io.github.pavlentygood.cellcapture.lobby.app.usecase.GetPartyUseCase
import io.github.pavlentygood.cellcapture.lobby.app.usecase.JoinPlayerUseCase
import io.github.pavlentygood.cellcapture.lobby.app.usecase.StartPartyUseCase
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Import
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories

@TestConfiguration
@EnableAutoConfiguration
@EnableJdbcRepositories("io.github.pavlentygood.cellcapture.lobby.app.output.db")
@Import(
    value = [
        CreatePartyUseCase::class,
        JoinPlayerUseCase::class,
        StartPartyUseCase::class,
        GetPartyUseCase::class,
        PartyFactory::class,
        SavePartyToDatabase::class,
        GeneratePlayerIdBySequence::class,
        GetPartyFromDatabase::class,
        GetPartyByPlayerFromDatabase::class,
        ErrorHandler::class
    ]
)
class IntegrationConfig
