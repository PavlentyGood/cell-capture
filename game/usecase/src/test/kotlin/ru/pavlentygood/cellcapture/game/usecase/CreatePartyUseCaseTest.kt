package ru.pavlentygood.cellcapture.game.usecase

import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.game.domain.CreateParty
import ru.pavlentygood.cellcapture.game.domain.party
import ru.pavlentygood.cellcapture.game.domain.partyInfo
import ru.pavlentygood.cellcapture.game.usecase.port.SaveParty

class CreatePartyUseCaseTest {

    @Test
    fun `create party`() {
        val partyInfo = partyInfo()
        val party = party()

        val partyFactory = mockk<CreateParty>()
        every { partyFactory(partyInfo) } returns party

        val saveParty = mockk<SaveParty>()
        justRun { saveParty(party) }

        val createParty = CreatePartyUseCase(partyFactory, saveParty)

        createParty(partyInfo)
    }
}
