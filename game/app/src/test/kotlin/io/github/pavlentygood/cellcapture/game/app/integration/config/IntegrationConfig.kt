package io.github.pavlentygood.cellcapture.game.app.integration.config

import io.github.pavlentygood.cellcapture.game.app.output.db.GetPartyByPlayerFromDatabase
import io.github.pavlentygood.cellcapture.game.app.output.db.SavePartyToDatabase
import io.github.pavlentygood.cellcapture.game.app.input.rest.ErrorHandler
import io.github.pavlentygood.cellcapture.game.app.usecase.CaptureCellsUseCase
import io.github.pavlentygood.cellcapture.game.app.usecase.RollDicesUseCase
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
