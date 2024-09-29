package ru.pavlentygood.cellcapture.persistence

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.domain.PlayerQueue
import ru.pavlentygood.cellcapture.domain.party
import ru.pavlentygood.cellcapture.domain.player
import ru.pavlentygood.cellcapture.domain.playerId

class GetPartyByPlayerFromDatabaseTest {

    @Test
    fun `get party by player`() {
        val player = player()

        val playerQueue = mockk<PlayerQueue>()
        every { playerQueue.players } returns listOf(player)

        val party = party(playerQueue = playerQueue)

        val saveParty = SavePartyToDatabase()
        saveParty(party)

        val getPartyByPlayer = GetPartyByPlayerFromDatabase(saveParty.parties)

        getPartyByPlayer(player.id) shouldBe party
    }

    @Test
    fun `get party by player - not found`() {
        val getPartyByPlayer = GetPartyByPlayerFromDatabase(mapOf())

        getPartyByPlayer(playerId()) shouldBe null
    }
}
