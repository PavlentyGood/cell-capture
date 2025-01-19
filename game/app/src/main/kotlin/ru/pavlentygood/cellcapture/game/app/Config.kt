package ru.pavlentygood.cellcapture.game.app

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import ru.pavlentygood.cellcapture.game.usecase.CaptureCellsUseCase
import ru.pavlentygood.cellcapture.game.usecase.CreatePartyUseCase
import ru.pavlentygood.cellcapture.game.usecase.RollUseCase
import ru.pavlentygood.cellcapture.game.domain.PartyFactory
import ru.pavlentygood.cellcapture.game.domain.RestoreParty
import ru.pavlentygood.cellcapture.game.persistence.GetPartyByPlayerFromDatabase
import ru.pavlentygood.cellcapture.game.persistence.SavePartyToDatabase

@Configuration
@ComponentScan("ru.pavlentygood.cellcapture.game.rest")
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
