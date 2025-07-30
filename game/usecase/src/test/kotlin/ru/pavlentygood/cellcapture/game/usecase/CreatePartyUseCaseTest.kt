package ru.pavlentygood.cellcapture.game.usecase

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.game.domain.CreateParty
import ru.pavlentygood.cellcapture.game.domain.party
import ru.pavlentygood.cellcapture.game.domain.partyInfo
import ru.pavlentygood.cellcapture.game.usecase.port.GetPartyByPlayer
import ru.pavlentygood.cellcapture.game.usecase.port.SaveParty

class CreatePartyUseCaseTest {

    @Test
    fun `create party`() {
        val party = party()
        val partyInfo = partyInfo()

        val getPartyByPlayer = mockk<GetPartyByPlayer>()
        every { getPartyByPlayer(partyInfo.ownerId) } returns null

        val partyFactory = mockk<CreateParty>()
        every { partyFactory(partyInfo) } returns party

        val saveParty = mockk<SaveParty>()
        justRun { saveParty(party) }

        val createParty = CreatePartyUseCase(getPartyByPlayer, partyFactory, saveParty)

        createParty(partyInfo)
    }

    @Test
    fun `party is not created twice`() {
        val party = party()
        val partyInfo = partyInfo(partyId = party.id)

        val getPartyByPlayer = mockk<GetPartyByPlayer>()
        every { getPartyByPlayer(partyInfo.ownerId) } returns party

        val partyFactory = mockk<CreateParty>()
        val saveParty = mockk<SaveParty>()
        val createParty = CreatePartyUseCase(getPartyByPlayer, partyFactory, saveParty)

        createParty(partyInfo) shouldBeLeft PartyAlreadyExists
    }
}
