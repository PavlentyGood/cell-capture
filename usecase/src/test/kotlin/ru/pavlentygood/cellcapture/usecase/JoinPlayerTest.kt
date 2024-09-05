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

class JoinPlayerTest {

    private val partyId = partyId()
    private val playerName = playerName()
    private val playerId = playerId()

    @Test
    fun `join player to party`() {
        val party = mockk<Party>()
        every { party.joinPlayer(playerName) } returns playerId.right()

        val getParty = mockk<GetParty>()
        every { getParty(partyId) } returns party.right()

        val saveParty = mockk<SaveParty>()
        justRun { saveParty(party) }

        val joinPlayer = JoinPlayer(getParty, saveParty)

        joinPlayer(partyId, playerName) shouldBeRight playerId
    }

    @Test
    fun `join player - party not found`() {
        val getParty = mockk<GetParty>()
        every { getParty(partyId) } returns PartyNotFoundUseCaseError.left()

        val saveParty = mockk<SaveParty>()

        val joinPlayer = JoinPlayer(getParty, saveParty)

        joinPlayer(partyId, playerName) shouldBeLeft PartyNotFoundUseCaseError
    }

    @Test
    fun `join player - player count limit exceeded`() {
        val party = mockk<Party>()
        every { party.joinPlayer(playerName) } returns PlayerCountLimitExceeded.left()

        val getParty = mockk<GetParty>()
        every { getParty(partyId) } returns party.right()

        val saveParty = mockk<SaveParty>()

        val joinPlayer = JoinPlayer(getParty, saveParty)

        joinPlayer(partyId, playerName) shouldBeLeft PlayerCountLimitExceededUseCaseError
    }
}
