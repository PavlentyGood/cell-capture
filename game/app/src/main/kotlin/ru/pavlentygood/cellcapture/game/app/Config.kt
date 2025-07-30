package ru.pavlentygood.cellcapture.game.app

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import ru.pavlentygood.cellcapture.game.domain.CreateParty
import ru.pavlentygood.cellcapture.game.domain.RestoreParty
import ru.pavlentygood.cellcapture.game.listening.ListeningConfig
import ru.pavlentygood.cellcapture.game.persistence.GetPartyByPlayerFromDatabase
import ru.pavlentygood.cellcapture.game.persistence.SavePartyToDatabase
import ru.pavlentygood.cellcapture.game.usecase.CaptureCellsUseCase
import ru.pavlentygood.cellcapture.game.usecase.CreatePartyUseCase
import ru.pavlentygood.cellcapture.game.usecase.GetPartyByPlayerUseCase
import ru.pavlentygood.cellcapture.game.usecase.RollUseCase

@Configuration
@ComponentScan("ru.pavlentygood.cellcapture.game.rest")
@Import(
    value = [
        CreatePartyUseCase::class,
        RollUseCase::class,
        CaptureCellsUseCase::class,
        GetPartyByPlayerUseCase::class,
        CreateParty::class,
        RestoreParty::class,
        SavePartyToDatabase::class,
        GetPartyByPlayerFromDatabase::class,
        ListeningConfig::class
    ]
)
class Config
