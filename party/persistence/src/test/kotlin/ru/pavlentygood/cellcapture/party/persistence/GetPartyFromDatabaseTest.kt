package ru.pavlentygood.cellcapture.party.persistence

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.party.domain.party
import ru.pavlentygood.cellcapture.party.domain.partyId

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
