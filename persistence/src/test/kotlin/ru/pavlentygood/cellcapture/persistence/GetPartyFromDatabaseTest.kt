package ru.pavlentygood.cellcapture.persistence

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.domain.party
import ru.pavlentygood.cellcapture.domain.partyId
import ru.pavlentygood.cellcapture.usecase.PartyNotFoundUseCaseError

class GetPartyFromDatabaseTest {

    private val party = party()
    private val getParty = GetPartyFromDatabase(mapOf(party.id to party))

    @Test
    fun `get party`() {
        getParty(party.id) shouldBeRight party
    }

    @Test
    fun `get party - not found`() {
        getParty(partyId()) shouldBeLeft PartyNotFoundUseCaseError
    }
}
