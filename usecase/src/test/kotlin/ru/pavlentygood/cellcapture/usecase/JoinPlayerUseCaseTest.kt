package ru.pavlentygood.cellcapture.usecase

import arrow.core.left
import arrow.core.right
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.domain.*

class JoinPlayerUseCaseTest {

    private val partyId = partyId()
    private val playerName = playerName()
    private val playerId = playerId()

    private val generatePlayerId = mockk<GeneratePlayerId>()

    @Test
    fun `join player to party`() {
        val party = mockk<Party>()

        every { party.joinPlayer(playerName, generatePlayerId) } returns playerId.right()

        val getParty = mockk<GetParty>()
        every { getParty(partyId) } returns party

        val saveParty = mockk<SaveParty>()
        justRun { saveParty(party) }

        val joinPlayer = JoinPlayerUseCase(getParty, saveParty, generatePlayerId)

        joinPlayer(partyId, playerName) shouldBeRight playerId
    }

    @Test
    fun `join player - party not found`() {
        val getParty = mockk<GetParty>()
        every { getParty(partyId) } returns null

        val saveParty = mockk<SaveParty>()

        val joinPlayer = JoinPlayerUseCase(getParty, saveParty, generatePlayerId)

        joinPlayer(partyId, playerName) shouldBeLeft PartyNotFoundUseCaseError
    }

    @Test
    fun `join player - player count limit reached`() {
        val party = mockk<Party>()
        every { party.joinPlayer(playerName, generatePlayerId) } returns Party.PlayerCountLimit.left()

        val getParty = mockk<GetParty>()
        every { getParty(partyId) } returns party

        val saveParty = mockk<SaveParty>()

        val joinPlayer = JoinPlayerUseCase(getParty, saveParty, generatePlayerId)

        joinPlayer(partyId, playerName) shouldBeLeft PlayerCountLimitUseCaseError
    }
}
