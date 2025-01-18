package ru.pavlentygood.cellcapture.party.app

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import ru.pavlentygood.cellcapture.party.domain.PartyFactory
import ru.pavlentygood.cellcapture.party.domain.RestoreParty
import ru.pavlentygood.cellcapture.party.persistence.GetPartyByPlayerFromDatabase
import ru.pavlentygood.cellcapture.party.persistence.SavePartyToDatabase
import ru.pavlentygood.cellcapture.party.usecase.*

@Configuration
@ComponentScan("ru.pavlentygood.cellcapture.party.rest")
class Config {

    @Bean
    fun createParty() = CreatePartyUseCase(partyFactory(), saveParty())

    @Bean
    fun partyFactory() = PartyFactory()

    @Bean
    fun restoreParty() = RestoreParty()

    @Bean
    fun saveParty() = SavePartyToDatabase()

    @Bean
    fun getPartyByPlayer() = GetPartyByPlayerFromDatabase(saveParty().parties, restoreParty())

    @Bean
    fun captureCells() = CaptureCellsUseCase(getPartyByPlayer(), saveParty())

    @Bean
    fun roll() = RollUseCase(getPartyByPlayer(), saveParty())
}
