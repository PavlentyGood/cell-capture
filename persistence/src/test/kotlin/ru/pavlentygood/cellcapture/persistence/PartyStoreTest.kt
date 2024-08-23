package ru.pavlentygood.cellcapture.persistence

import io.kotest.matchers.maps.shouldContain
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.domain.Party
import ru.pavlentygood.cellcapture.domain.PartyId
import java.util.*

class PartyStoreTest {

    @Test
    fun `save party`() {
        val saveParty = PartyStore()
        val party = Party(PartyId(UUID.randomUUID()))

        saveParty(party)

        saveParty.store shouldContain Pair(party.id, party)
    }
}
