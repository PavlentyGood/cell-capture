package ru.pavlentygood.cellcapture.lobby.app.integration.config

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Import
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories
import ru.pavlentygood.cellcapture.lobby.domain.PartyFactory
import ru.pavlentygood.cellcapture.lobby.persistence.GeneratePlayerIdBySequence
import ru.pavlentygood.cellcapture.lobby.persistence.GetPartyByPlayerFromDatabase
import ru.pavlentygood.cellcapture.lobby.persistence.GetPartyFromDatabase
import ru.pavlentygood.cellcapture.lobby.persistence.SavePartyToDatabase
import ru.pavlentygood.cellcapture.lobby.rest.endpoint.ErrorHandler
import ru.pavlentygood.cellcapture.lobby.usecase.CreatePartyUseCase
import ru.pavlentygood.cellcapture.lobby.usecase.GetPartyUseCase
import ru.pavlentygood.cellcapture.lobby.usecase.JoinPlayerUseCase
import ru.pavlentygood.cellcapture.lobby.usecase.StartPartyUseCase

@TestConfiguration
@EnableAutoConfiguration
@EnableJdbcRepositories("ru.pavlentygood.cellcapture.lobby.persistence")
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
