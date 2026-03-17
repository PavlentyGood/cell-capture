package io.github.pavlentygood.cellcapture.lobby.app

import io.github.pavlentygood.cellcapture.lobby.domain.PartyFactory
import io.github.pavlentygood.cellcapture.lobby.persistence.GeneratePlayerIdBySequence
import io.github.pavlentygood.cellcapture.lobby.persistence.GetPartyByPlayerFromDatabase
import io.github.pavlentygood.cellcapture.lobby.persistence.GetPartyFromDatabase
import io.github.pavlentygood.cellcapture.lobby.persistence.SavePartyToDatabase
import io.github.pavlentygood.cellcapture.lobby.usecase.CreatePartyUseCase
import io.github.pavlentygood.cellcapture.lobby.usecase.GetPartyUseCase
import io.github.pavlentygood.cellcapture.lobby.usecase.JoinPlayerUseCase
import io.github.pavlentygood.cellcapture.lobby.usecase.StartPartyUseCase
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@EnableScheduling
@ComponentScan("io.github.pavlentygood.cellcapture.lobby.rest")
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
        GetPartyByPlayerFromDatabase::class
    ]
)
class Config
