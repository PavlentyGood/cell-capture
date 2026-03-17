package io.github.pavlentygood.cellcapture.game.app

import io.github.pavlentygood.cellcapture.game.domain.CreateParty
import io.github.pavlentygood.cellcapture.game.persistence.GetPartyByPlayerFromDatabase
import io.github.pavlentygood.cellcapture.game.persistence.SavePartyToDatabase
import io.github.pavlentygood.cellcapture.game.usecase.CaptureCellsUseCase
import io.github.pavlentygood.cellcapture.game.usecase.CreatePartyUseCase
import io.github.pavlentygood.cellcapture.game.usecase.GetPartyByPlayerUseCase
import io.github.pavlentygood.cellcapture.game.usecase.RollDicesUseCase
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@ComponentScan("io.github.pavlentygood.cellcapture.game.rest")
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
