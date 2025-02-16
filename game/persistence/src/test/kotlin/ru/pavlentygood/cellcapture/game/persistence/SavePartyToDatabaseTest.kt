package ru.pavlentygood.cellcapture.game.persistence

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.ContextConfiguration
import ru.pavlentygood.cellcapture.game.domain.*
import ru.pavlentygood.cellcapture.game.usecase.port.SaveParty
import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.kernel.domain.Player
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId

@JdbcTest
@ContextConfiguration(classes = [TestPersistenceConfig::class])
class SavePartyToDatabaseTest {

    @Autowired
    private lateinit var jdbcTemplate: NamedParameterJdbcTemplate
    @Autowired
    private lateinit var saveParty: SaveParty

    @Test
    fun `save new party`() {
        val owner = player()
        val player = player()
        val cells = cells()
        cells[1][2] = owner.id
        cells[3][4] = player.id
        val party = party(
            dices = Dices.notRolled(),
            field = field(cells()),
            owner = owner,
            currentPlayer = player
        )

        saveParty(party)

        party.check()
    }

    @Test
    fun `save existing party`() {
        val owner = player()
        val player = player()
        val cells = cells()
        cells[1][2] = owner.id
        cells[3][4] = player.id
        val party = party(
            dices = Dices.roll(),
            field = field(cells()),
            owner = owner,
            currentPlayer = player
        )
        saveParty(party)
        cells[5][6] = owner.id
        val changedParty = party(
            id = party.id,
            dices = Dices.notRolled(),
            field = field(cells()),
            owner = owner,
            currentPlayer = player
        )

        saveParty(changedParty)

        changedParty.check()
    }

    private fun Party.check() {
        checkExistsInDb()
        players.forEach {
            it.checkExistsInDb(id)
        }
        cells.forEachIndexed { y, row ->
            row.forEachIndexed { x, playerId ->
                if (playerId != Field.nonePlayerId) {
                    checkExistsInDb(x, y, playerId, id)
                }
            }
        }
    }

    private fun Party.checkExistsInDb() {
        val fields = jdbcTemplate.queryForMap(
            "select * from parties where id = :id",
            mapOf("id" to id.toUUID())
        )
        fields["completed"] shouldBe (this is CompletedParty)
        fields["first_dice"] shouldBe (dices as? RolledDices)?.first?.value
        fields["second_dice"] shouldBe (dices as? RolledDices)?.second?.value
        fields["owner_id"] shouldBe ownerId.toInt()
        fields["current_player_id"] shouldBe currentPlayerId.toInt()
        fields["created"] shouldNotBe null
        fields["updated"] shouldNotBe null
    }

    private fun Player.checkExistsInDb(partyId: PartyId) {
        val fields = jdbcTemplate.queryForMap(
            "select * from players where id = :id",
            mapOf("id" to id.toInt())
        )
        fields["name"] shouldBe name.toStringValue()
        fields["party_id"] shouldBe partyId.toUUID()
    }

    private fun checkExistsInDb(x: Int, y: Int, playerId: PlayerId, partyId: PartyId) {
        val sql = """
            select exists(
                select 1 from cells
                where player_id = :player_id
                and x = :x
                and y = :y
            )
        """

        val params = mapOf(
            "player_id" to playerId.toInt(),
            "party_id" to partyId.toUUID(),
            "x" to x,
            "y" to y
        )

        val exists = jdbcTemplate.queryForObject(sql, params, Boolean::class.java)
        exists shouldBe true
    }
}
