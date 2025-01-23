package ru.pavlentygood.cellcapture.lobby.persistence

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.kernel.domain.playerId
import ru.pavlentygood.cellcapture.lobby.domain.PlayerList
import ru.pavlentygood.cellcapture.lobby.domain.RestoreParty
import ru.pavlentygood.cellcapture.lobby.domain.party
import ru.pavlentygood.cellcapture.lobby.domain.player

class GetPartyByPlayerFromDatabaseTest {

    @Test
    fun `get party by player`() {
        val player = player()

        val playerList = mockk<PlayerList>()
        every { playerList.players } returns listOf(player)

        val party = party(playerList = playerList)

        val saveParty = SavePartyToDatabase()
        saveParty(party)

        val getPartyByPlayer = GetPartyByPlayerFromDatabase(saveParty.parties, RestoreParty())

        getPartyByPlayer(player.id)!!.apply {
            id shouldBe party.id
            started shouldBe party.started
            playerLimit shouldBe party.playerLimit
            players shouldBe party.players
            ownerId shouldBe ownerId
        }
    }

    @Test
    fun `get party by player - not found`() {
        val getPartyByPlayer = GetPartyByPlayerFromDatabase(mapOf(), RestoreParty())

        getPartyByPlayer(playerId()) shouldBe null
    }
}
