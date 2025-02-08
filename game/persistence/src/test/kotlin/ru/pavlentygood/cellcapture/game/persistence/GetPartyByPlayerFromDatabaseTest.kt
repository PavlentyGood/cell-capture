package ru.pavlentygood.cellcapture.game.persistence

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.game.domain.RestoreParty
import ru.pavlentygood.cellcapture.game.domain.party
import ru.pavlentygood.cellcapture.kernel.domain.playerId

class GetPartyByPlayerFromDatabaseTest {

    @Test
    fun `get party by player`() {
        val party = party()

        val saveParty = SavePartyToDatabase()
        saveParty(party)

        val getPartyByPlayer = GetPartyByPlayerFromDatabase(saveParty.parties, RestoreParty())

        getPartyByPlayer(party.ownerId)!!.apply {
            id shouldBe party.id
            dices shouldBe party.dices
            cells shouldBe party.cells
            players shouldBe party.players
            currentPlayerId shouldBe party.currentPlayerId
            ownerId shouldBe ownerId
        }
    }

    @Test
    fun `get party by player - not found`() {
        val getPartyByPlayer = GetPartyByPlayerFromDatabase(mutableMapOf(), RestoreParty())
        getPartyByPlayer(playerId()) shouldBe null
    }
}
