package ru.pavlentygood.cellcapture.lobby.usecase

import arrow.core.left
import arrow.core.right
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.kernel.domain.partyId
import ru.pavlentygood.cellcapture.kernel.domain.playerId
import ru.pavlentygood.cellcapture.kernel.domain.playerName
import ru.pavlentygood.cellcapture.lobby.domain.GeneratePlayerId
import ru.pavlentygood.cellcapture.lobby.domain.Party
import ru.pavlentygood.cellcapture.lobby.usecase.port.GetParty
import ru.pavlentygood.cellcapture.lobby.usecase.port.SaveParty

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

        joinPlayer(partyId, playerName) shouldBeLeft JoinPlayerUseCaseError.PartyNotFoundUseCaseError
    }

    @Test
    fun `join player - domain errors`() {
        mapOf(
            Party.AlreadyStarted to JoinPlayerUseCaseError.AlreadyStartedUseCaseError,
            Party.PlayerCountLimit to JoinPlayerUseCaseError.PlayerCountLimitUseCaseError
        ).forEach { (domainError, useCaseError) ->
            val party = mockk<Party>()
            every { party.joinPlayer(playerName, generatePlayerId) } returns domainError.left()

            val getParty = mockk<GetParty>()
            every { getParty(partyId) } returns party

            val saveParty = mockk<SaveParty>()

            val joinPlayer = JoinPlayerUseCase(getParty, saveParty, generatePlayerId)

            joinPlayer(partyId, playerName) shouldBeLeft useCaseError
        }
    }
}
