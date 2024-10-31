package ru.pavlentygood.cellcapture.usecase

import arrow.core.left
import arrow.core.right
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.data.row
import io.kotest.inspectors.forAll
import io.mockk.*
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.domain.Party
import ru.pavlentygood.cellcapture.domain.dicePair
import ru.pavlentygood.cellcapture.domain.playerId

class RollTest {

    @Test
    fun `roll - player not found`() = with { roll, getPartyByPlayer, _ ->
        val playerId = playerId()

        every { getPartyByPlayer(playerId) } returns null

        roll(playerId) shouldBeLeft Roll.PlayerNotFound
    }

    @Test
    fun `roll - domain errors`() {
        listOf(
            row(Party.PlayerNotCurrent, Roll.PlayerNotCurrent),
            row(Party.DicesAlreadyRolled, Roll.DicesAlreadyRolled)
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

    private fun with(test: (Roll, GetPartyByPlayer, SaveParty) -> Unit) {
        val getPartyByPlayer = mockk<GetPartyByPlayer>()
        val saveParty = mockk<SaveParty>()
        val roll = Roll(getPartyByPlayer, saveParty)
        test(roll, getPartyByPlayer, saveParty)
    }
}