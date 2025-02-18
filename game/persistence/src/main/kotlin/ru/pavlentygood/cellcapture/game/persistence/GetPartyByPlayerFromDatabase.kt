package ru.pavlentygood.cellcapture.game.persistence

import arrow.core.getOrElse
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import ru.pavlentygood.cellcapture.game.domain.Cell
import ru.pavlentygood.cellcapture.game.domain.Field
import ru.pavlentygood.cellcapture.game.domain.Party
import ru.pavlentygood.cellcapture.game.domain.RestoreParty
import ru.pavlentygood.cellcapture.game.usecase.port.GetPartyByPlayer
import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.kernel.domain.Player
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId
import java.util.*

class GetPartyByPlayerFromDatabase(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val restoreParty: RestoreParty
) : GetPartyByPlayer {

    override operator fun invoke(playerId: PlayerId): Party? {
        val partyDto: PartyDto = getParty(playerId) ?: return null
        return restoreParty(
            id = PartyId(partyDto.id),
            completed = partyDto.completed,
            dices = partyDto.restoreDices(),
            players = getPlayers(partyDto.id),
            cells = getCells(partyDto.id),
            currentPlayerId = PlayerId(partyDto.currentPlayerId),
            ownerId = PlayerId(partyDto.ownerId)
        ).getOrElse {
            error("Restore party error: $it. #${partyDto.id}")
        }
    }

    private fun getParty(playerId: PlayerId): PartyDto? {
        val sql = """
             select p.* from parties p
             join players pl on pl.party_id = p.id
             where pl.id = :player_id
        """
        val params = mapOf("player_id" to playerId.toInt())
        return jdbcTemplate.query(sql, params, partyDtoMapper)
            .singleOrNull()
    }

    private fun getPlayers(partyId: UUID): List<Player> {
        val sql = "select * from players where party_id = :party_id"
        val params = mapOf("party_id" to partyId)
        return jdbcTemplate.query(sql, params, playerMapper)
    }

    private fun getCells(partyId: UUID): Array<Array<Cell>> {
        val sql = "select * from cells where party_id = :party_id"
        val params = mapOf("party_id" to partyId)
        val cells = Array(Field.HEIGHT) { y ->
            Array(Field.WIDTH) { x ->
                Cell(Field.nonePlayerId, x, y)
            }
        }
        jdbcTemplate.query(sql, params, cellMapper)
            .forEach { cells[it.y][ it.x] = it }
        return cells
    }
}
