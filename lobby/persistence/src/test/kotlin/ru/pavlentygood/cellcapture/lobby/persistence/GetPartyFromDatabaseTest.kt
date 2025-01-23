package ru.pavlentygood.cellcapture.lobby.persistence

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.kernel.domain.partyId
import ru.pavlentygood.cellcapture.lobby.domain.party

class GetPartyFromDatabaseTest {

    private val party = party()
    private val getParty = GetPartyFromDatabase(mapOf(party.id to party))

    @Test
    fun `get party`() {
        getParty(party.id) shouldBe party
    }

    @Test
    fun `get party - not found`() {
        getParty(partyId()) shouldBe null
    }
}
