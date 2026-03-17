package io.github.pavlentygood.cellcapture.lobby.app.integration.config

import io.github.pavlentygood.cellcapture.lobby.domain.PartyFactory
import io.github.pavlentygood.cellcapture.lobby.persistence.GeneratePlayerIdBySequence
import io.github.pavlentygood.cellcapture.lobby.persistence.GetPartyByPlayerFromDatabase
import io.github.pavlentygood.cellcapture.lobby.persistence.GetPartyFromDatabase
import io.github.pavlentygood.cellcapture.lobby.persistence.SavePartyToDatabase
import io.github.pavlentygood.cellcapture.lobby.rest.endpoint.ErrorHandler
import io.github.pavlentygood.cellcapture.lobby.usecase.CreatePartyUseCase
import io.github.pavlentygood.cellcapture.lobby.usecase.GetPartyUseCase
import io.github.pavlentygood.cellcapture.lobby.usecase.JoinPlayerUseCase
import io.github.pavlentygood.cellcapture.lobby.usecase.StartPartyUseCase
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Import
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories

@TestConfiguration
@EnableAutoConfiguration
@EnableJdbcRepositories("io.github.pavlentygood.cellcapture.lobby.persistence")
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
