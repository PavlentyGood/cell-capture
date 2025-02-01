package ru.pavlentygood.cellcapture.game.domain

import arrow.core.left
import arrow.core.right
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.kernel.domain.playerId

class PartyTest {

    @Test
    fun `roll dices`() {
        val player = player()
        val party = party(
            dicePair = null,
            currentPlayer = player
        )

        val rolledDice = party.roll(player.id).shouldBeRight()

        party.dicePair.shouldNotBeNull()
        party.dicePair shouldBe rolledDice
    }

    @Test
    fun `roll dices - player not current`() {
        val party = party()
        party.roll(playerId()) shouldBeLeft Party.PlayerNotCurrent
    }

    @Test
    fun `roll dices - dices already rolled`() {
        val player = player()
        val dicePair = dicePair()
        val party = party(
            dicePair = dicePair,
            currentPlayer = player
        )

        party.roll(player.id) shouldBeLeft Party.DicesAlreadyRolled

        party.dicePair shouldBe dicePair
    }

    @Test
    fun `capture cells`() {
        val area = area()
        val player = player()
        val nextPlayer = player()

        val field = mockk<Field>()
        every { field.capture(player.id, area) } returns Unit.right()

        val party = party(
            dicePair = dicePairFor(area),
            field = field,
            currentPlayer = player,
            otherPlayers = listOf(player, nextPlayer)
        )

        party.capture(player.id, area) shouldBeRight Unit

        party.dicePair shouldBe null
        party.currentPlayerId shouldBe nextPlayer.id
    }

    @Test
    fun `capture cells - player not current`() {
        val area = area()
        val party = party()

        party.capture(playerId(), area) shouldBeLeft Party.PlayerNotCurrent
    }

    @Test
    fun `capture cells - dices not rolled`() {
        val area = area()
        val player = player()
        val party = party(
            dicePair = null,
            currentPlayer = player
        )

        party.capture(player.id, area) shouldBeLeft Party.DicesNotRolled
    }

    @Test
    fun `capture cells - mismatched area`() {
        val area = area()
        val player = player()

        val dicePair = mockk<DicePair>()
        every { dicePair.isMatched(area) } returns false

        val party = party(
            dicePair = dicePair,
            currentPlayer = player
        )

        party.capture(player.id, area) shouldBeLeft Party.MismatchedArea
    }

    @Test
    fun `capture cells - inaccessible area`() {
        val area = area()
        val player = player()
        val field = mockk<Field>()
        val dicePair = dicePairFor(area)
        val party = party(
            dicePair = dicePair,
            field = field,
            currentPlayer = player
        )

        every { field.capture(player.id, area) } returns Party.InaccessibleArea.left()

        party.capture(player.id, area) shouldBeLeft Party.InaccessibleArea
    }

    private fun dicePairFor(area: Area) =
        mockk<DicePair>().also {
            every { it.isMatched(area) } returns true
        }
}
