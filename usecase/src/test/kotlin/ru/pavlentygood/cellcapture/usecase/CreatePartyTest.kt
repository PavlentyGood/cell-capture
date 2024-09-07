package ru.pavlentygood.cellcapture.usecase

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.domain.PartyFactory
import ru.pavlentygood.cellcapture.domain.party

class CreatePartyTest {

    @Test
    fun `create party`() {
        val party = party()

        val partyFactory = mockk<PartyFactory>()
        every { partyFactory.create() } returns party

        val saveParty = mockk<SaveParty>()
        justRun { saveParty(party) }

        val createParty = CreateParty(partyFactory, saveParty)

        createParty() shouldBe party.id
    }
}
