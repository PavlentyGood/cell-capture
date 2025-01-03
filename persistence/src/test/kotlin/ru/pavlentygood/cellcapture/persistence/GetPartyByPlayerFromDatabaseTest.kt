package ru.pavlentygood.cellcapture.persistence

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.domain.*

class GetPartyByPlayerFromDatabaseTest {

    @Test
    fun `get party by player`() {
        val player = player()

        val playerQueue = mockk<PlayerQueue>()
        every { playerQueue.players } returns listOf(player)
        every { playerQueue.currentPlayerId } returns player.id

        val party = party(playerQueue = playerQueue)

        val saveParty = SavePartyToDatabase()
        saveParty(party)

        val getPartyByPlayer = GetPartyByPlayerFromDatabase(saveParty.parties, RestoreParty())

        getPartyByPlayer(player.id)!!.apply {
            id shouldBe party.id
            playerLimit shouldBe party.playerLimit
            status shouldBe party.status
            dicePair shouldBe party.dicePair
            getCells() shouldBe party.getCells()
            players shouldBe party.players
            currentPlayerId shouldBe party.currentPlayerId
            ownerId shouldBe ownerId
        }
    }

    @Test
    fun `get party by player - not found`() {
        val getPartyByPlayer = GetPartyByPlayerFromDatabase(mapOf(), RestoreParty())

        getPartyByPlayer(playerId()) shouldBe null
    }
}
