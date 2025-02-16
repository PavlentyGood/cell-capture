package ru.pavlentygood.cellcapture.game.persistence

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.test.context.ContextConfiguration
import ru.pavlentygood.cellcapture.game.domain.party
import ru.pavlentygood.cellcapture.game.usecase.port.GetPartyByPlayer
import ru.pavlentygood.cellcapture.game.usecase.port.SaveParty
import ru.pavlentygood.cellcapture.kernel.domain.playerId

@JdbcTest
@ContextConfiguration(classes = [TestPersistenceConfig::class])
class GetPartyByPlayerFromDatabaseTest {

    @Autowired
    private lateinit var saveParty: SaveParty
    @Autowired
    private lateinit var getPartyByPlayer: GetPartyByPlayer

    @Test
    fun `get party by player`() {
        val party = party()
        saveParty(party)

        getPartyByPlayer(party.ownerId)!!.apply {
            completed shouldBe party.completed
            id shouldBe party.id
            dices shouldBe party.dices
            cells shouldBe party.cells
            players shouldBe party.players
            ownerId shouldBe party.ownerId
            currentPlayerId shouldBe party.currentPlayerId
        }
    }

    @Test
    fun `get party by player - not found`() {
        getPartyByPlayer(playerId()) shouldBe null
    }
}
