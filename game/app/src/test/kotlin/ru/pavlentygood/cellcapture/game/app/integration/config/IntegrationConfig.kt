package ru.pavlentygood.cellcapture.game.app.integration.config

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Import
import ru.pavlentygood.cellcapture.game.persistence.GetPartyByPlayerFromDatabase
import ru.pavlentygood.cellcapture.game.persistence.SavePartyToDatabase
import ru.pavlentygood.cellcapture.game.rest.endpoint.ErrorHandler
import ru.pavlentygood.cellcapture.game.usecase.CaptureCellsUseCase
import ru.pavlentygood.cellcapture.game.usecase.RollDicesUseCase

@TestConfiguration
@EnableAutoConfiguration
@Import(
    value = [
        RollDicesUseCase::class,
        CaptureCellsUseCase::class,
        SavePartyToDatabase::class,
        GetPartyByPlayerFromDatabase::class,
        ErrorHandler::class
    ]
)
class IntegrationConfig
