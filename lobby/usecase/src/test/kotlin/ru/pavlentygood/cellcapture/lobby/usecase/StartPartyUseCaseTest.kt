package ru.pavlentygood.cellcapture.lobby.usecase

import arrow.core.left
import arrow.core.right
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.lobby.domain.Party
import ru.pavlentygood.cellcapture.lobby.domain.playerId
import ru.pavlentygood.cellcapture.lobby.usecase.port.GetPartyByPlayer

class StartPartyUseCaseTest {

    @Test
    fun `start party`() {
        val playerId = playerId()

        val party = mockk<Party>()
        every { party.start(playerId) } returns Unit.right()

        val getPartyByPlayer = mockk<GetPartyByPlayer>()
        every { getPartyByPlayer(playerId) } returns party

        val startParty = StartPartyUseCase(getPartyByPlayer)

        startParty(playerId) shouldBeRight Unit
    }

    @Test
    fun `start party - player not found`() {
        val playerId = playerId()

        val getPartyByPlayer = mockk<GetPartyByPlayer>()
        every { getPartyByPlayer(playerId) } returns null

        val startParty = StartPartyUseCase(getPartyByPlayer)

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

            val startParty = StartPartyUseCase(getPartyByPlayer)

            startParty(playerId) shouldBeLeft useCaseError
        }
    }
}
