package ru.pavlentygood.cellcapture.party.app

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import ru.pavlentygood.cellcapture.party.domain.PartyFactory
import ru.pavlentygood.cellcapture.party.domain.RestoreParty
import ru.pavlentygood.cellcapture.party.usecase.*
import ru.pavlentygood.cellcapture.party.persistence.GeneratePlayerIdBySequence
import ru.pavlentygood.cellcapture.party.persistence.GetPartyByPlayerFromDatabase
import ru.pavlentygood.cellcapture.party.persistence.GetPartyFromDatabase
import ru.pavlentygood.cellcapture.party.persistence.SavePartyToDatabase
import ru.pavlentygood.cellcapture.party.usecase.*

@Configuration
@ComponentScan("ru.pavlentygood.cellcapture.rest")
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
    fun startParty() = StartPartyUseCase(getPartyByPlayer())

    @Bean
    fun getPartyByPlayer() = GetPartyByPlayerFromDatabase(saveParty().parties, restoreParty())

    @Bean
    fun captureCells() = CaptureCellsUseCase(getPartyByPlayer(), saveParty())

    @Bean
    fun roll() = RollUseCase(getPartyByPlayer(), saveParty())
}
