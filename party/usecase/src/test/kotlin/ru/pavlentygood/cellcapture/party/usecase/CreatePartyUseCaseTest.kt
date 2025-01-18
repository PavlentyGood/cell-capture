package ru.pavlentygood.cellcapture.party.usecase

import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.party.domain.PartyFactory
import ru.pavlentygood.cellcapture.party.domain.party
import ru.pavlentygood.cellcapture.party.domain.partyInfo
import ru.pavlentygood.cellcapture.party.usecase.port.SaveParty

class CreatePartyUseCaseTest {

    @Test
    fun `create party`() {
        val partyInfo = partyInfo()
        val party = party()

        val partyFactory = mockk<PartyFactory>()
        every { partyFactory.create(partyInfo) } returns party

        val saveParty = mockk<SaveParty>()
        justRun { saveParty(party) }

        val createParty = CreatePartyUseCase(partyFactory, saveParty)

        createParty(partyInfo)
    }
}
