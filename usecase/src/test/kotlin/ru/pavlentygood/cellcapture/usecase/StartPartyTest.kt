package ru.pavlentygood.cellcapture.usecase

import arrow.core.left
import arrow.core.right
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.domain.Party
import ru.pavlentygood.cellcapture.domain.playerId

class StartPartyTest {

    @Test
    fun `start party`() {
        val playerId = playerId()

        val party = mockk<Party>()
        every { party.start(playerId) } returns Unit.right()

        val getPartyByPlayer = mockk<GetPartyByPlayer>()
        every { getPartyByPlayer(playerId) } returns party

        val startParty = StartParty(getPartyByPlayer)

        startParty(playerId) shouldBeRight Unit
    }

    @Test
    fun `start party - player not found`() {
        val playerId = playerId()

        val getPartyByPlayer = mockk<GetPartyByPlayer>()
        every { getPartyByPlayer(playerId) } returns null

        val startParty = StartParty(getPartyByPlayer)

        startParty(playerId) shouldBeLeft StartParty.PlayerNotFound
    }

    @Test
    fun `start party - domain errors`() {
        mapOf(
            Party.PlayerNotOwner to StartParty.PlayerNotOwner,
            Party.AlreadyStarted to StartParty.AlreadyStarted,
            Party.AlreadyCompleted to StartParty.AlreadyCompleted
        ).forEach { (domainError, useCaseError) ->
            val playerId = playerId()

            val party = mockk<Party>()
            every { party.start(playerId) } returns domainError.left()

            val getPartyByPlayer = mockk<GetPartyByPlayer>()
            every { getPartyByPlayer(playerId) } returns party

            val startParty = StartParty(getPartyByPlayer)

            startParty(playerId) shouldBeLeft useCaseError
        }
    }
}
