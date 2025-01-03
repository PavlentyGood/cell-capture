package ru.pavlentygood.cellcapture.persistence

import io.kotest.matchers.maps.shouldContain
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.domain.party

class SavePartyToDatabaseTest {

    @Test
    fun `save party`() {
        val party = party()
        val saveParty = SavePartyToDatabase()

        saveParty(party)

        saveParty.parties shouldContain Pair(party.id, party)
    }
}
