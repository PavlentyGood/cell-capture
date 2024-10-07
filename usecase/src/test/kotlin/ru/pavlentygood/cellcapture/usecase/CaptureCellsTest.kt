package ru.pavlentygood.cellcapture.usecase

import arrow.core.left
import arrow.core.right
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.domain.Party
import ru.pavlentygood.cellcapture.domain.area
import ru.pavlentygood.cellcapture.domain.playerId

class CaptureCellsTest {

    @Test
    fun `capture cells`() {
        val playerId = playerId()
        val area = area()

        val party = mockk<Party>()
        every { party.capture(playerId, area) } returns Unit.right()

        val getPartyByPlayer = mockk<GetPartyByPlayer>()
        every { getPartyByPlayer(playerId) } returns party

        val saveParty = mockk<SaveParty>()
        justRun { saveParty(party) }

        val captureCells = CaptureCells(getPartyByPlayer, saveParty)

        captureCells(playerId, area) shouldBeRight Unit
    }

    @Test
    fun `capture cells - player not found`() {
        val playerId = playerId()

        val getPartyByPlayer = mockk<GetPartyByPlayer>()
        every { getPartyByPlayer(playerId) } returns null

        val saveParty = mockk<SaveParty>()

        val captureCells = CaptureCells(getPartyByPlayer, saveParty)

        captureCells(playerId, area()) shouldBeLeft CaptureCells.PlayerNotFound
    }

    @Test
    fun `capture cells - domain errors`() {
        mapOf(
            Party.PlayerNotCurrent to CaptureCells.PlayerNotCurrent,
            Party.DicesNotRolled to CaptureCells.DicesNotRolled,
            Party.MismatchedArea to CaptureCells.MismatchedArea,
            Party.InaccessibleArea to CaptureCells.InaccessibleArea
        ).forEach { (domainError, useCaseError) ->
            val playerId = playerId()
            val area = area()

            val party = mockk<Party>()
            every { party.capture(playerId, area) } returns domainError.left()

            val getPartyByPlayer = mockk<GetPartyByPlayer>()
            every { getPartyByPlayer(playerId) } returns party

            val saveParty = mockk<SaveParty>()

            val captureCells = CaptureCells(getPartyByPlayer, saveParty)

            captureCells(playerId, area) shouldBeLeft useCaseError
        }
    }
}
