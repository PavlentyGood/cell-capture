package io.github.pavlentygood.cellcapture.game.app.config

import io.github.pavlentygood.cellcapture.game.app.output.db.GetPartyByPlayerFromDatabase
import io.github.pavlentygood.cellcapture.game.app.output.db.SavePartyToDatabase
import io.github.pavlentygood.cellcapture.game.app.usecase.CaptureCellsUseCase
import io.github.pavlentygood.cellcapture.game.app.usecase.CreatePartyUseCase
import io.github.pavlentygood.cellcapture.game.app.usecase.GetPartyByPlayerUseCase
import io.github.pavlentygood.cellcapture.game.app.usecase.RollDicesUseCase
import io.github.pavlentygood.cellcapture.game.domain.CreateParty
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@ComponentScan("io.github.pavlentygood.cellcapture.game.app.input.rest")
@Import(
    value = [
        CreatePartyUseCase::class,
        RollDicesUseCase::class,
        CaptureCellsUseCase::class,
        GetPartyByPlayerUseCase::class,
        CreateParty::class,
        SavePartyToDatabase::class,
        GetPartyByPlayerFromDatabase::class,
        ListeningConfig::class
    ]
)
class Config
