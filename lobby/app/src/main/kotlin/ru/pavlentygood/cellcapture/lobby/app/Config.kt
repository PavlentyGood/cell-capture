package ru.pavlentygood.cellcapture.lobby.app

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.core.JdbcTemplate
import ru.pavlentygood.cellcapture.lobby.domain.GeneratePlayerId
import ru.pavlentygood.cellcapture.lobby.domain.PartyFactory
import ru.pavlentygood.cellcapture.lobby.domain.RestoreParty
import ru.pavlentygood.cellcapture.lobby.persistence.*
import ru.pavlentygood.cellcapture.lobby.publishing.PublishingConfig
import ru.pavlentygood.cellcapture.lobby.usecase.CreatePartyUseCase
import ru.pavlentygood.cellcapture.lobby.usecase.GetPartyUseCase
import ru.pavlentygood.cellcapture.lobby.usecase.JoinPlayerUseCase
import ru.pavlentygood.cellcapture.lobby.usecase.StartPartyUseCase
import ru.pavlentygood.cellcapture.lobby.usecase.port.GetParty
import ru.pavlentygood.cellcapture.lobby.usecase.port.GetPartyByPlayer
import ru.pavlentygood.cellcapture.lobby.usecase.port.PublishPartyStartedEvent
import ru.pavlentygood.cellcapture.lobby.usecase.port.SaveParty

@Configuration
@ComponentScan("ru.pavlentygood.cellcapture.lobby.rest")
@EnableJpaRepositories("ru.pavlentygood.cellcapture.lobby.persistence")
@EntityScan("ru.pavlentygood.cellcapture.lobby.persistence")
@Import(
    value = [
        GetPartyUseCase::class,
        PublishingConfig::class
    ]
)
class Config {

    @Bean
    fun createParty(partyFactory: PartyFactory, saveParty: SaveParty) =
        CreatePartyUseCase(partyFactory, saveParty)

    @Bean
    fun partyFactory(generatePlayerId: GeneratePlayerId) =
        PartyFactory(generatePlayerId)

    @Bean
    fun restoreParty() =
        RestoreParty()

    @Bean
    fun saveParty(partyRepository: PartyRepository) =
        SavePartyToDatabase(partyRepository)

    @Bean
    fun joinPlayer(getParty: GetParty, saveParty: SaveParty, generatePlayerId: GeneratePlayerId) =
        JoinPlayerUseCase(getParty, saveParty, generatePlayerId)

    @Bean
    fun getParty(partyRepository: PartyRepository) =
        GetPartyFromDatabase(partyRepository, mapPartyToDomain())

    @Bean
    fun generatePlayerId(jdbcTemplate: JdbcTemplate) =
        GeneratePlayerIdBySequence(jdbcTemplate)

    @Bean
    fun startParty(
        getPartyByPlayer: GetPartyByPlayer,
        saveParty: SaveParty,
        publishPartyStartedEvent: PublishPartyStartedEvent
    ) =
        StartPartyUseCase(getPartyByPlayer, saveParty, publishPartyStartedEvent)

    @Bean
    fun getPartyByPlayer(partyRepository: PartyRepository) =
        GetPartyByPlayerFromDatabase(partyRepository, mapPartyToDomain())

    @Bean
    fun mapPartyToDomain() =
        MapPartyToDomain(restoreParty())
}
