package ru.pavlentygood.cellcapture.persistence

import io.kotest.matchers.maps.shouldContain
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.domain.Party
import ru.pavlentygood.cellcapture.domain.partyId

class SavePartyToDatabaseTest {

    @Test
    fun `save party`() {
        val saveParty = SavePartyToDatabase()
        val party = Party(partyId())

        saveParty(party)

        saveParty.store shouldContain Pair(party.id, party)
    }
}
