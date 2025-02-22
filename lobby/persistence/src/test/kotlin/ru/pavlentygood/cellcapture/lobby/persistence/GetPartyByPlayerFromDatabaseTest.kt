package ru.pavlentygood.cellcapture.lobby.persistence

import io.kotest.matchers.shouldBe
import io.mockk.mockk
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.kernel.domain.playerId
import ru.pavlentygood.cellcapture.lobby.domain.RestoreParty
import ru.pavlentygood.cellcapture.lobby.domain.party
import ru.pavlentygood.cellcapture.lobby.domain.player

class GetPartyByPlayerFromDatabaseTest {

    @Test
    fun `get party by player`() {
        val player = player()
        val party = party(
            otherPlayers = listOf(player)
        )

        val saveParty = SavePartyToDatabase(mockk<PartyRepository>())
        saveParty(party)

        val getPartyByPlayer = GetPartyByPlayerFromDatabase(mapOf(), RestoreParty())

        getPartyByPlayer(player.id)!!.apply {
            id shouldBe party.id
            started shouldBe party.started
            playerLimit shouldBe party.playerLimit
            getPlayers() shouldBe party.getPlayers()
            ownerId shouldBe ownerId
        }
    }

    @Test
    fun `get party by player - not found`() {
        val getPartyByPlayer = GetPartyByPlayerFromDatabase(mapOf(), RestoreParty())
        getPartyByPlayer(playerId()) shouldBe null
    }
}
