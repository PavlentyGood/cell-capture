package ru.pavlentygood.cellcapture.game.usecase

import arrow.core.left
import arrow.core.right
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.game.domain.*
import ru.pavlentygood.cellcapture.game.usecase.port.GetPartyByPlayer
import ru.pavlentygood.cellcapture.game.usecase.port.SaveParty
import ru.pavlentygood.cellcapture.kernel.domain.playerId

class CaptureCellsUseCaseTest {

    @Test
    fun `capture cells`() {
        val playerId = playerId()
        val area = area()

        val party = mockk<ActiveParty>()
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

        captureCells(playerId, area()) shouldBeLeft CaptureCellsUseCaseError.PlayerNotFound
    }

    @Test
    fun `capture cells - domain errors`() {
        mapOf(
            PlayerNotCurrent to CaptureCellsUseCaseError.PlayerNotCurrent,
            DicesNotRolled to CaptureCellsUseCaseError.DicesNotRolled,
            MismatchedArea to CaptureCellsUseCaseError.MismatchedArea,
            InaccessibleArea to CaptureCellsUseCaseError.InaccessibleArea,
            PartyCompleted to CaptureCellsUseCaseError.PartyCompleted
        ).forEach { (domainError, useCaseError) ->
            val playerId = playerId()
            val area = area()

            val party = mockk<ActiveParty>()
            every { party.capture(playerId, area) } returns domainError.left()

            val getPartyByPlayer = mockk<GetPartyByPlayer>()
            every { getPartyByPlayer(playerId) } returns party

            val saveParty = mockk<SaveParty>()

            val captureCells = CaptureCellsUseCase(getPartyByPlayer, saveParty)

            captureCells(playerId, area) shouldBeLeft useCaseError
        }
    }
}
