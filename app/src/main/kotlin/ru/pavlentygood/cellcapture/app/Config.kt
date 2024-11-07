package ru.pavlentygood.cellcapture.app

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import ru.pavlentygood.cellcapture.domain.PartyFactory
import ru.pavlentygood.cellcapture.domain.RestoreParty
import ru.pavlentygood.cellcapture.persistence.GeneratePlayerIdBySequence
import ru.pavlentygood.cellcapture.persistence.GetPartyByPlayerFromDatabase
import ru.pavlentygood.cellcapture.persistence.GetPartyFromDatabase
import ru.pavlentygood.cellcapture.persistence.SavePartyToDatabase
import ru.pavlentygood.cellcapture.usecase.*

@Configuration
@ComponentScan("ru.pavlentygood.cellcapture.rest")
class Config {

    @Bean
    fun createParty() = CreateParty(partyFactory(), saveParty())

    @Bean
    fun partyFactory() = PartyFactory(generatePlayerId())

    @Bean
    fun restoreParty() = RestoreParty()

    @Bean
    fun saveParty() = SavePartyToDatabase()

    @Bean
    fun joinPlayer() = JoinPlayer(getParty(), saveParty(), generatePlayerId())

    @Bean
    fun getParty() = GetPartyFromDatabase(saveParty().parties)

    @Bean
    fun generatePlayerId() = GeneratePlayerIdBySequence()

    @Bean
    fun startParty() = StartParty(getPartyByPlayer())

    @Bean
    fun getPartyByPlayer() = GetPartyByPlayerFromDatabase(saveParty().parties, restoreParty())

    @Bean
    fun captureCells() = CaptureCells(getPartyByPlayer(), saveParty())

    @Bean
    fun roll() = Roll(getPartyByPlayer(), saveParty())
}
