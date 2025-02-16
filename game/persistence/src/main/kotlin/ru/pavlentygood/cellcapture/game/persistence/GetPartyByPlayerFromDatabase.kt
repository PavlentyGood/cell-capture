package ru.pavlentygood.cellcapture.game.persistence

import arrow.core.getOrElse
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import ru.pavlentygood.cellcapture.game.domain.*
import ru.pavlentygood.cellcapture.game.usecase.port.GetPartyByPlayer
import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.kernel.domain.Player
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId
import ru.pavlentygood.cellcapture.kernel.domain.PlayerName
import java.sql.ResultSet
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
            dices = mapDices(partyDto),
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
        val mapper = { rs: ResultSet, _: Int ->
            PartyDto(
                id = rs.getObject("id", UUID::class.java),
                completed = rs.getBoolean("completed"),
                firstDice = rs.getIntOrNull("first_dice"),
                secondDice = rs.getIntOrNull("second_dice"),
                ownerId = rs.getInt("owner_id"),
                currentPlayerId = rs.getInt("current_player_id"),
            )
        }
        return jdbcTemplate.query(sql, params, mapper)
            .singleOrNull()
    }

    private fun getPlayers(partyId: UUID): List<Player> {
        val sql = "select * from players where party_id = :party_id"
        val params = mapOf("party_id" to partyId)
        val mapper = { rs: ResultSet, _: Int ->
            val name = rs.getString("name")
            Player(
                id = PlayerId(rs.getInt("id")),
                name = PlayerName.from(name).getOrElse {
                    error("Illegal player name: $name")
                }
            )
        }
        return jdbcTemplate.query(sql, params, mapper)
    }

    private fun getCells(partyId: UUID): Array<Array<PlayerId>> {
        val sql = "select * from cells where party_id = :party_id"
        val params = mapOf("party_id" to partyId)
        val mapper = { rs: ResultSet, _: Int ->
            Cell(
                playerId = PlayerId(rs.getInt("player_id")),
                x = rs.getInt("x"),
                y = rs.getInt("y")
            )
        }
        val cells = Array(Field.HEIGHT) {
            Array(Field.WIDTH) { Field.nonePlayerId }
        }
        jdbcTemplate.query(sql, params, mapper).forEach {
            cells[it.y][ it.x] = it.playerId
        }
        return cells
    }

    private fun mapDices(partyDto: PartyDto): Dices {
        return if (partyDto.firstDice == null && partyDto.secondDice == null) {
            Dices.notRolled()
        } else {
            RolledDices(
                first = mapDice(partyDto.firstDice),
                second = mapDice(partyDto.secondDice),
            )
        }
    }

    private fun mapDice(value: Int?): Dice {
        return Dice.from(value!!).getOrElse {
            error("Invalid dice value: $value")
        }
    }

    private fun ResultSet.getIntOrNull(columnName: String) =
        getObject(columnName, Integer::class.java)?.toInt()
}
