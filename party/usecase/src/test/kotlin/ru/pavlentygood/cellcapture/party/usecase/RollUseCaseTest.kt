package ru.pavlentygood.cellcapture.party.usecase

import arrow.core.left
import arrow.core.right
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.data.row
import io.kotest.inspectors.forAll
import io.mockk.*
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.party.domain.Party
import ru.pavlentygood.cellcapture.party.domain.dicePair
import ru.pavlentygood.cellcapture.party.domain.playerId
import ru.pavlentygood.cellcapture.party.usecase.port.GetPartyByPlayer
import ru.pavlentygood.cellcapture.party.usecase.port.SaveParty

class RollUseCaseTest {

    @Test
    fun `roll - player not found`() = with { roll, getPartyByPlayer, _ ->
        val playerId = playerId()

        every { getPartyByPlayer(playerId) } returns null

        roll(playerId) shouldBeLeft RollUseCase.PlayerNotFound
    }

    @Test
    fun `roll - domain errors`() {
        listOf(
            row(Party.PlayerNotCurrent, RollUseCase.PlayerNotCurrent),
            row(Party.DicesAlreadyRolled, RollUseCase.DicesAlreadyRolled)
        ).forAll { (domainError, useCaseError) ->
            with { roll, getPartyByPlayer, _ ->
                val playerId = playerId()
                val party = mockk<Party>()

                every { party.roll(playerId) } returns domainError.left()
                every { getPartyByPlayer(playerId) } returns party

                roll(playerId) shouldBeLeft useCaseError
            }
        }
    }

    @Test
    fun `roll dices`() = with { roll, getPartyByPlayer, saveParty ->
        val playerId = playerId()
        val dicePair = dicePair()
        val party = mockk<Party>()

        every { party.roll(playerId) } returns dicePair.right()
        every { getPartyByPlayer(playerId) } returns party
        justRun { saveParty(party) }

        roll(playerId) shouldBeRight dicePair
    }

    private fun with(test: (RollUseCase, GetPartyByPlayer, SaveParty) -> Unit) {
        val getPartyByPlayer = mockk<GetPartyByPlayer>()
        val saveParty = mockk<SaveParty>()
        val roll = RollUseCase(getPartyByPlayer, saveParty)
        test(roll, getPartyByPlayer, saveParty)
    }
}
