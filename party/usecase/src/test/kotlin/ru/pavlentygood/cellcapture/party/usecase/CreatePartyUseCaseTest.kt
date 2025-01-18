package ru.pavlentygood.cellcapture.party.usecase

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.party.domain.PartyFactory
import ru.pavlentygood.cellcapture.party.domain.party
import ru.pavlentygood.cellcapture.party.domain.playerName
import ru.pavlentygood.cellcapture.party.usecase.port.SaveParty

class CreatePartyUseCaseTest {

    @Test
    fun `create party`() {
        val ownerName = playerName()
        val party = party()

        val partyFactory = mockk<PartyFactory>()
        every { partyFactory.create(ownerName) } returns party

        val saveParty = mockk<SaveParty>()
        justRun { saveParty(party) }

        val createParty = CreatePartyUseCase(partyFactory, saveParty)

        createParty(ownerName) shouldBe CreatePartyResult(party.id, party.ownerId)
    }
}
