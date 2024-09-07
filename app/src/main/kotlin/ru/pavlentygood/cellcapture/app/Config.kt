package ru.pavlentygood.cellcapture.app

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import ru.pavlentygood.cellcapture.domain.PartyFactory
import ru.pavlentygood.cellcapture.persistence.GeneratePlayerIdBySequence
import ru.pavlentygood.cellcapture.persistence.GetPartyFromDatabase
import ru.pavlentygood.cellcapture.persistence.SavePartyToDatabase
import ru.pavlentygood.cellcapture.usecase.CreateParty
import ru.pavlentygood.cellcapture.usecase.JoinPlayer

@Configuration
@ComponentScan("ru.pavlentygood.cellcapture.rest")
class Config {

    @Bean
    fun createParty() = CreateParty(partyFactory(), saveParty())

    @Bean
    fun partyFactory() = PartyFactory()

    @Bean
    fun saveParty() = SavePartyToDatabase()

    @Bean
    fun joinPlayer() = JoinPlayer(getParty(), saveParty(), generatePlayerId())

    @Bean
    fun getParty() = GetPartyFromDatabase(saveParty().parties)

    @Bean
    fun generatePlayerId() = GeneratePlayerIdBySequence()
}
