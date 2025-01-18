package ru.pavlentygood.cellcapture.lobby.app

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import ru.pavlentygood.cellcapture.lobby.domain.PartyFactory
import ru.pavlentygood.cellcapture.lobby.domain.RestoreParty
import ru.pavlentygood.cellcapture.lobby.persistence.GeneratePlayerIdBySequence
import ru.pavlentygood.cellcapture.lobby.persistence.GetPartyByPlayerFromDatabase
import ru.pavlentygood.cellcapture.lobby.persistence.GetPartyFromDatabase
import ru.pavlentygood.cellcapture.lobby.persistence.SavePartyToDatabase
import ru.pavlentygood.cellcapture.lobby.usecase.*

@Configuration
@ComponentScan("ru.pavlentygood.cellcapture.lobby.rest")
class Config {

    @Bean
    fun createParty() = CreatePartyUseCase(partyFactory(), saveParty())

    @Bean
    fun partyFactory() = PartyFactory(generatePlayerId())

    @Bean
    fun restoreParty() = RestoreParty()

    @Bean
    fun saveParty() = SavePartyToDatabase()

    @Bean
    fun joinPlayer() = JoinPlayerUseCase(getParty(), saveParty(), generatePlayerId())

    @Bean
    fun getParty() = GetPartyFromDatabase(saveParty().parties)

    @Bean
    fun generatePlayerId() = GeneratePlayerIdBySequence()

    @Bean
    fun startParty() = StartPartyUseCase(getPartyByPlayer(), saveParty())

    @Bean
    fun getPartyByPlayer() = GetPartyByPlayerFromDatabase(saveParty().parties, restoreParty())
}
