package ru.pavlentygood.cellcapture.game.persistence

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.annotation.Transactional
import ru.pavlentygood.cellcapture.game.domain.Cell
import ru.pavlentygood.cellcapture.game.domain.Field
import ru.pavlentygood.cellcapture.game.domain.Party
import ru.pavlentygood.cellcapture.game.domain.RolledDices
import ru.pavlentygood.cellcapture.game.usecase.port.SaveParty
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId

@Transactional
class SavePartyToDatabase(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) : SaveParty {

    override fun invoke(party: Party) {
        if (party.isExists()) {
            party.updatePartyInfo()
            party.updateCells()
        } else {
            party.insertPartyInfo()
            party.insertPlayers()
            party.insertCells(party.getCapturedCells())
        }
    }

    private fun Party.isExists(): Boolean {
        val sql = "select exists(select 1 from parties where id = :id)"
        val params = mapOf("id" to id.toUUID())
        return jdbcTemplate.queryForObject(sql, params, Boolean::class.java)!!
    }

    private fun Party.updatePartyInfo() {
        val sql = """
            update parties set
            completed = :completed,
            first_dice = :first_dice,
            second_dice = :second_dice,
            owner_id = :owner_id,
            current_player_id = :current_player_id,
            updated = current_timestamp
            where id = :id
        """
        jdbcTemplate.update(sql, toParams())
    }

    private fun Party.updateCells() {
        val savedCells = getSavedCells()
        val capturedCells = getCapturedCells()
        val newCells = capturedCells.minus(savedCells)
        insertCells(newCells)
    }

    private fun Party.getSavedCells(): Set<Cell> {
        val sql = "select player_id, x, y from cells where party_id = :party_id"
        val params = mapOf("party_id" to id.toUUID())
        return jdbcTemplate.query(sql, params) { rs, _ ->
            Cell(
                playerId = PlayerId(rs.getInt("player_id")),
                x = rs.getInt("x"),
                y = rs.getInt("y")
            )
        }.toSet()
    }

    private fun Party.getCapturedCells(): List<Cell> {
        return cells.mapIndexed { y, row ->
            row.mapIndexed { x, playerId ->
                Cell(
                    playerId = playerId,
                    x = x,
                    y = y
                )
            }
        }.flatten()
            .filter { it.playerId != Field.nonePlayerId }
    }

    private fun Party.insertPartyInfo() {
        val sql = """
            insert into parties (id, completed, first_dice, second_dice, owner_id, current_player_id)
            values (:id, :completed, :first_dice, :second_dice, :owner_id, :current_player_id)
        """
        jdbcTemplate.update(sql, toParams())
    }

    private fun Party.insertPlayers() {
        val sql = """       
            insert into players (id, name, party_id)
            values (:id, :name, :party_id)
        """

        val params = players.map {
            mapOf(
                "id" to it.id.toInt(),
                "name" to it.name.toStringValue(),
                "party_id" to this.id.toUUID()
            )
        }.toTypedArray()

        jdbcTemplate.batchUpdate(sql, params)
    }

    private fun Party.insertCells(cells: List<Cell>) {
        val sql = """
            insert into cells (party_id, player_id, x, y)
            values (:party_id, :player_id, :x, :y)
        """

        val params = cells.map {
            mapOf(
                "party_id" to id.toUUID(),
                "player_id" to it.playerId.toInt(),
                "x" to it.x,
                "y" to it.y
            )
        }.toTypedArray()

        jdbcTemplate.batchUpdate(sql, params)
    }

    private fun Party.toParams(): Map<String, Any?> {
        return mapOf(
            "id" to id.toUUID(),
            "completed" to completed,
            "first_dice" to (dices as? RolledDices)?.first?.value,
            "second_dice" to (dices as? RolledDices)?.second?.value,
            "owner_id" to ownerId.toInt(),
            "current_player_id" to currentPlayerId.toInt()
        )
    }
}
