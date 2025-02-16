package ru.pavlentygood.cellcapture.game.app

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import ru.pavlentygood.cellcapture.game.domain.CreateParty
import ru.pavlentygood.cellcapture.game.domain.RestoreParty
import ru.pavlentygood.cellcapture.game.persistence.GetPartyByPlayerFromDatabase
import ru.pavlentygood.cellcapture.game.persistence.SavePartyToDatabase
import ru.pavlentygood.cellcapture.game.usecase.CaptureCellsUseCase
import ru.pavlentygood.cellcapture.game.usecase.CreatePartyUseCase
import ru.pavlentygood.cellcapture.game.usecase.RollUseCase
import ru.pavlentygood.cellcapture.game.usecase.port.GetPartyByPlayer
import ru.pavlentygood.cellcapture.game.usecase.port.SaveParty

@Configuration
@ComponentScan("ru.pavlentygood.cellcapture.game.rest")
class Config {

    @Bean
    fun createParty(saveParty: SaveParty) =
        CreatePartyUseCase(partyFactory(), saveParty)

    @Bean
    fun partyFactory() = CreateParty()

    @Bean
    fun restoreParty() = RestoreParty()

    @Bean
    fun saveParty(jdbcTemplate: NamedParameterJdbcTemplate) =
        SavePartyToDatabase(jdbcTemplate)

    @Bean
    fun getPartyByPlayer(jdbcTemplate: NamedParameterJdbcTemplate) =
        GetPartyByPlayerFromDatabase(jdbcTemplate, restoreParty())

    @Bean
    fun captureCells(getPartyByPlayer: GetPartyByPlayer, saveParty: SaveParty) =
        CaptureCellsUseCase(getPartyByPlayer, saveParty)

    @Bean
    fun roll(getPartyByPlayer: GetPartyByPlayer, saveParty: SaveParty) =
        RollUseCase(getPartyByPlayer, saveParty)
}
