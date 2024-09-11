package ru.pavlentygood.cellcapture.persistence

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.domain.party
import ru.pavlentygood.cellcapture.domain.player
import ru.pavlentygood.cellcapture.domain.playerId

class GetPartyByPlayerFromDatabaseTest {

    @Test
    fun `get party by player`() {
        val player = player()
        val party = party(players = listOf(player))

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
