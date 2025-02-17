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
        cells.setCell(owner.id, 2, 1)
        cells.setCell(player.id, 4, 3)
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
        cells.setCell(owner.id, 2, 1)
        cells.setCell(player.id, 4, 3)
        val party = party(
            dices = Dices.roll(),
            field = field(cells()),
            owner = owner,
            currentPlayer = player
        )
        saveParty(party)
        cells.setCell(owner.id, 6, 5)
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
        players.forEach { it.checkExistsInDb(id) }
        cells.capturedCells().forEach { it.checkExistsInDb(id) }
    }

    private fun Party.checkExistsInDb() {
        val fields = jdbcTemplate.queryForMap(
            "select * from parties where id = :id",
            mapOf("id" to id.toUUID())
        )
        fields["completed"] shouldBe completed
        fields["first_dice"] shouldBe dices.firstValue
        fields["second_dice"] shouldBe dices.secondValue
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

    private fun Cell.checkExistsInDb(partyId: PartyId) {
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
