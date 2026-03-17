package io.github.pavlentygood.cellcapture.game.app.integration.config

import io.github.pavlentygood.cellcapture.game.persistence.GetPartyByPlayerFromDatabase
import io.github.pavlentygood.cellcapture.game.persistence.SavePartyToDatabase
import io.github.pavlentygood.cellcapture.game.rest.endpoint.ErrorHandler
import io.github.pavlentygood.cellcapture.game.usecase.CaptureCellsUseCase
import io.github.pavlentygood.cellcapture.game.usecase.RollDicesUseCase
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Import

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
