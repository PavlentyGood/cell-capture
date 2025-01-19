package ru.pavlentygood.cellcapture.game.domain

import arrow.core.left
import arrow.core.right
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.junit.jupiter.api.Test

class PartyTest {

    @Test
    fun `roll - player not current`() {
        val playerQueue = mockk<PlayerQueue>()
        every { playerQueue.currentPlayerId } returns playerId()

        val party = party(
            playerQueue = playerQueue
        )

        party.roll(playerId()) shouldBeLeft Party.PlayerNotCurrent

        party.completed shouldBe false
    }

    @Test
    fun `roll - dices already rolled`() {
        val currentPlayerId = playerId()
        val dicePair = dicePair()

        val playerQueue = mockk<PlayerQueue>()
        every { playerQueue.currentPlayerId } returns currentPlayerId

        val party = party(
            dicePair = dicePair,
            playerQueue = playerQueue
        )

        party.roll(currentPlayerId) shouldBeLeft Party.DicesAlreadyRolled

        party.completed shouldBe false
        party.dicePair shouldBe dicePair
    }

    @Test
    fun `roll dices`() {
        val currentPlayerId = playerId()

        val playerQueue = mockk<PlayerQueue>()
        every { playerQueue.currentPlayerId } returns currentPlayerId

        val party = party(
            dicePair = null,
            playerQueue = playerQueue
        )

        val rolledDice = party.roll(currentPlayerId).shouldBeRight()

        party.completed shouldBe false
        party.dicePair shouldNotBe null
        party.dicePair shouldBe rolledDice
    }

    @Test
    fun `capture - player not current`() {
        val area = area()

        val playerQueue = mockk<PlayerQueue>()
        every { playerQueue.currentPlayerId } returns playerId()

        val party = party(
            playerQueue = playerQueue
        )

        party.capture(playerId(), area) shouldBeLeft Party.PlayerNotCurrent

        party.completed shouldBe false
    }

    @Test
    fun `capture - dices not rolled`() {
        val area = area()
        val currentPlayerId = playerId()

        val playerQueue = mockk<PlayerQueue>()
        every { playerQueue.currentPlayerId } returns currentPlayerId

        val party = party(
            dicePair = null,
            playerQueue = playerQueue
        )

        party.capture(currentPlayerId, area) shouldBeLeft Party.DicesNotRolled

        party.completed shouldBe false
        party.dicePair shouldBe null
    }

    @Test
    fun `capture - mismatched area`() {
        val area = area()
        val currentPlayerId = playerId()

        val dicePair = mockk<DicePair>()
        every { dicePair.isMatched(area) } returns false

        val playerQueue = mockk<PlayerQueue>()
        every { playerQueue.currentPlayerId } returns currentPlayerId

        val party = party(
            dicePair = dicePair,
            playerQueue = playerQueue
        )

        party.capture(currentPlayerId, area) shouldBeLeft Party.MismatchedArea

        party.completed shouldBe false
        party.dicePair shouldBe dicePair
    }

    @Test
    fun `capture - inaccessible area`() {
        val area = area()
        val field = mockk<Field>()
        val currentPlayerId = playerId()
        val dicePair = dicePairFor(area)

        val playerQueue = mockk<PlayerQueue>()
        every { playerQueue.currentPlayerId } returns currentPlayerId

        val party = party(
            dicePair = dicePair,
            field = field,
            playerQueue = playerQueue
        )

        every { field.capture(currentPlayerId, area) } returns Party.InaccessibleArea.left()

        party.capture(currentPlayerId, area) shouldBeLeft Party.InaccessibleArea

        party.completed shouldBe false
        party.dicePair shouldBe dicePair
    }

    @Test
    fun `capture cells`() {
        val area = area()
        val currentPlayerId = playerId()

        val field = mockk<Field>()
        every { field.capture(currentPlayerId, area) } returns Unit.right()

        val playerQueue = mockk<PlayerQueue>()
        every { playerQueue.currentPlayerId } returns currentPlayerId
        justRun { playerQueue.changeCurrentPlayer() }

        val party = party(
            dicePair = dicePairFor(area),
            field = field,
            playerQueue = playerQueue
        )

        party.capture(currentPlayerId, area) shouldBeRight Unit

        party.completed shouldBe false
        party.dicePair shouldBe null
    }

    private fun dicePairFor(area: Area) =
        mockk<DicePair>().also {
            every { it.isMatched(area) } returns true
        }
}
