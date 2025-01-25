package ru.pavlentygood.cellcapture.lobby.usecase

import arrow.core.left
import arrow.core.right
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.kernel.domain.playerId
import ru.pavlentygood.cellcapture.lobby.domain.Party
import ru.pavlentygood.cellcapture.lobby.usecase.port.GetPartyByPlayer
import ru.pavlentygood.cellcapture.lobby.usecase.port.SaveParty

class StartPartyErrorPartyUseCaseTest {

    @Test
    fun `start party`() {
        val playerId = playerId()

        val party = mockk<Party>()
        every { party.start(playerId) } returns Unit.right()

        val getPartyByPlayer = mockk<GetPartyByPlayer>()
        every { getPartyByPlayer(playerId) } returns party

        val saveParty = mockk<SaveParty>()
        justRun { saveParty(party) }

        val startParty = StartPartyUseCase(getPartyByPlayer, saveParty)

        startParty(playerId) shouldBeRight Unit
    }

    @Test
    fun `start party - player not found`() {
        val playerId = playerId()

        val getPartyByPlayer = mockk<GetPartyByPlayer>()
        every { getPartyByPlayer(playerId) } returns null

        val saveParty = mockk<SaveParty>()

        val startParty = StartPartyUseCase(getPartyByPlayer, saveParty)

        startParty(playerId) shouldBeLeft StartPartyUseCase.PlayerNotFound
    }

    @Test
    fun `start party - domain errors`() {
        mapOf(
            Party.PlayerNotOwner to StartPartyUseCase.PlayerNotOwner,
            Party.AlreadyStarted to StartPartyUseCase.AlreadyStarted
        ).forEach { (domainError, useCaseError) ->
            val playerId = playerId()

            val party = mockk<Party>()
            every { party.start(playerId) } returns domainError.left()

            val getPartyByPlayer = mockk<GetPartyByPlayer>()
            every { getPartyByPlayer(playerId) } returns party

            val saveParty = mockk<SaveParty>()

            val startParty = StartPartyUseCase(getPartyByPlayer, saveParty)

            startParty(playerId) shouldBeLeft useCaseError
        }
    }
}
