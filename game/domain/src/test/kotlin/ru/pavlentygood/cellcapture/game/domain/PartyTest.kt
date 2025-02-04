package ru.pavlentygood.cellcapture.game.domain

import arrow.core.left
import arrow.core.right
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.kernel.domain.playerId

class PartyTest {

    @Test
    fun `roll dices`() {
        val player = player()
        val party = party(
            dices = Dices.notRolled(),
            currentPlayer = player
        )

        val rolledDices = party.roll(player.id).shouldBeRight()

        party.dices shouldBe rolledDices
        rolledDices shouldNotBe Dices.notRolled()
    }

    @Test
    fun `roll dices - player not current`() {
        val party = party()
        party.roll(playerId()) shouldBeLeft Party.PlayerNotCurrent
    }

    @Test
    fun `roll dices - dices already rolled`() {
        val player = player()
        val dices = dices()
        val party = party(
            dices = dices,
            currentPlayer = player
        )

        party.roll(player.id) shouldBeLeft Party.DicesAlreadyRolled

        party.dices shouldBe dices
    }

    @Test
    fun `capture cells`() {
        val area = area()
        val player = player()
        val nextPlayer = player()

        val field = mockk<Field>()
        every { field.capture(player.id, area) } returns Unit.right()

        val party = party(
            dices = dicesFor(area),
            field = field,
            currentPlayer = player,
            otherPlayers = listOf(player, nextPlayer)
        )

        party.capture(player.id, area) shouldBeRight Unit

        party.dices shouldBe Dices.notRolled()
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
            dices = Dices.notRolled(),
            currentPlayer = player
        )

        party.capture(player.id, area) shouldBeLeft Party.DicesNotRolled
    }

    @Test
    fun `capture cells - mismatched area`() {
        val area = area()
        val player = player()

        val dices = mockk<Dices>()
        every { dices.isMatched(area) } returns false.right()

        val party = party(
            dices = dices,
            currentPlayer = player
        )

        party.capture(player.id, area) shouldBeLeft Party.MismatchedArea
    }

    @Test
    fun `capture cells - inaccessible area`() {
        val area = area()
        val player = player()
        val field = mockk<Field>()
        val dices = dicesFor(area)
        val party = party(
            dices = dices,
            field = field,
            currentPlayer = player
        )

        every { field.capture(player.id, area) } returns Party.InaccessibleArea.left()

        party.capture(player.id, area) shouldBeLeft Party.InaccessibleArea
    }

    private fun dicesFor(area: Area) =
        mockk<Dices>().also {
            every { it.isMatched(area) } returns true.right()
        }
}
