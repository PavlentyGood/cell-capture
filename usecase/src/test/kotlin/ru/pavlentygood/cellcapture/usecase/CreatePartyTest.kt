package ru.pavlentygood.cellcapture.usecase

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.domain.PartyFactory
import ru.pavlentygood.cellcapture.domain.partyAndOwner

class CreatePartyTest {

    @Test
    fun `create party`() {
        val (party, owner) = partyAndOwner()

        val partyFactory = mockk<PartyFactory>()
        every { partyFactory.create(owner.name) } returns party

        val saveParty = mockk<SaveParty>()
        justRun { saveParty(party) }

        val createParty = CreateParty(partyFactory, saveParty)

        createParty(owner.name) shouldBe CreatePartyResult(party.id, owner.id)
    }
}
