package ru.pavlentygood.cellcapture.lobby.app

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import ru.pavlentygood.cellcapture.lobby.domain.PartyFactory
import ru.pavlentygood.cellcapture.lobby.domain.RestoreParty
import ru.pavlentygood.cellcapture.lobby.persistence.*
import ru.pavlentygood.cellcapture.lobby.publishing.PublishingConfig
import ru.pavlentygood.cellcapture.lobby.usecase.CreatePartyUseCase
import ru.pavlentygood.cellcapture.lobby.usecase.GetPartyUseCase
import ru.pavlentygood.cellcapture.lobby.usecase.JoinPlayerUseCase
import ru.pavlentygood.cellcapture.lobby.usecase.StartPartyUseCase

@Configuration
@ComponentScan("ru.pavlentygood.cellcapture.lobby.rest")
@EnableJpaRepositories("ru.pavlentygood.cellcapture.lobby.persistence")
@EntityScan("ru.pavlentygood.cellcapture.lobby.persistence")
@Import(
    value = [
        CreatePartyUseCase::class,
        JoinPlayerUseCase::class,
        StartPartyUseCase::class,
        GetPartyUseCase::class,
        PartyFactory::class,
        RestoreParty::class,
        SavePartyToDatabase::class,
        GeneratePlayerIdBySequence::class,
        GetPartyFromDatabase::class,
        GetPartyByPlayerFromDatabase::class,
        MapPartyToDomain::class,
        PublishingConfig::class
    ]
)
class Config
