package ru.pavlentygood.cellcapture.usecase

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.domain.PartyFactory
import ru.pavlentygood.cellcapture.domain.party
import ru.pavlentygood.cellcapture.domain.playerName

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
