package ru.pavlentygood.cellcapture.party.usecase

import arrow.core.left
import arrow.core.right
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.party.domain.Party
import ru.pavlentygood.cellcapture.party.domain.area
import ru.pavlentygood.cellcapture.party.domain.playerId
import ru.pavlentygood.cellcapture.party.usecase.port.GetPartyByPlayer
import ru.pavlentygood.cellcapture.party.usecase.port.SaveParty

class CaptureCellsUseCaseTest {

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

        val captureCells = CaptureCellsUseCase(getPartyByPlayer, saveParty)

        captureCells(playerId, area) shouldBeRight Unit
    }

    @Test
    fun `capture cells - player not found`() {
        val playerId = playerId()

        val getPartyByPlayer = mockk<GetPartyByPlayer>()
        every { getPartyByPlayer(playerId) } returns null

        val saveParty = mockk<SaveParty>()

        val captureCells = CaptureCellsUseCase(getPartyByPlayer, saveParty)

        captureCells(playerId, area()) shouldBeLeft CaptureCellsUseCase.PlayerNotFound
    }

    @Test
    fun `capture cells - domain errors`() {
        mapOf(
            Party.PlayerNotCurrent to CaptureCellsUseCase.PlayerNotCurrent,
            Party.DicesNotRolled to CaptureCellsUseCase.DicesNotRolled,
            Party.MismatchedArea to CaptureCellsUseCase.MismatchedArea,
            Party.InaccessibleArea to CaptureCellsUseCase.InaccessibleArea
        ).forEach { (domainError, useCaseError) ->
            val playerId = playerId()
            val area = area()

            val party = mockk<Party>()
            every { party.capture(playerId, area) } returns domainError.left()

            val getPartyByPlayer = mockk<GetPartyByPlayer>()
            every { getPartyByPlayer(playerId) } returns party

            val saveParty = mockk<SaveParty>()

            val captureCells = CaptureCellsUseCase(getPartyByPlayer, saveParty)

            captureCells(playerId, area) shouldBeLeft useCaseError
        }
    }
}
