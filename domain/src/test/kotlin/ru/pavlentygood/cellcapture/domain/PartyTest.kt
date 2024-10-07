package ru.pavlentygood.cellcapture.domain

import arrow.core.left
import arrow.core.right
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.junit.jupiter.api.Test

class PartyTest {

    @Test
    fun `join player`() {
        val playerId = playerId()
        val name = playerName()

        val playerQueue = mockk<PlayerQueue>()
        justRun {
            playerQueue.add(match {
                it.id == playerId &&
                        it.name == name
            })
        }
        every { playerQueue.players } returns listOf(mockk())

        val party = party(
            playerLimit = 3,
            playerQueue = playerQueue
        )

        val generatePlayerId = { playerId }

        party.joinPlayer(name, generatePlayerId) shouldBeRight playerId

        party.status shouldBe Party.Status.NEW
    }

    @Test
    fun `join player - limit`() {
        val playerId = playerId()
        val name = playerName()

        val playerQueue = mockk<PlayerQueue>()
        every { playerQueue.players } returns listOf(mockk())

        val party = party(
            playerLimit = 1,
            playerQueue = playerQueue
        )

        val generatePlayerId = { playerId }

        party.joinPlayer(name, generatePlayerId) shouldBeLeft PlayerCountLimitExceeded

        party.status shouldBe Party.Status.NEW
    }

    @Test
    fun `start party`() {
        val playerQueue = mockk<PlayerQueue>()
        every { playerQueue.players } returns listOf(mockk(), mockk())

        val party = party(
            playerQueue = playerQueue
        )

        party.start(party.ownerId) shouldBeRight Unit

        party.status shouldBe Party.Status.STARTED
    }

    @Test
    fun `start party - player not owner`() {
        val playerQueue = mockk<PlayerQueue>()
        every { playerQueue.players } returns listOf(mockk(), mockk())

        val party = party(
            playerQueue = playerQueue
        )

        party.start(playerId()) shouldBeLeft Party.PlayerNotOwner

        party.status shouldBe Party.Status.NEW
    }

    @Test
    fun `start party - too few players`() {
        val playerQueue = mockk<PlayerQueue>()
        every { playerQueue.players } returns listOf(mockk())

        val party = party(
            playerQueue = playerQueue
        )

        party.start(party.ownerId) shouldBeLeft Party.TooFewPlayers

        party.status shouldBe Party.Status.NEW
    }

    @Test
    fun `start party - already started`() {
        val playerQueue = mockk<PlayerQueue>()
        every { playerQueue.players } returns listOf(mockk(), mockk())

        val party = party(
            status = Party.Status.STARTED,
            playerQueue = playerQueue
        )

        party.start(party.ownerId) shouldBeLeft Party.AlreadyStarted

        party.status shouldBe Party.Status.STARTED
    }

    @Test
    fun `start party - already completed`() {
        val playerQueue = mockk<PlayerQueue>()
        every { playerQueue.players } returns listOf(mockk(), mockk())

        val party = party(
            status = Party.Status.COMPLETED,
            playerQueue = playerQueue
        )

        party.start(party.ownerId) shouldBeLeft Party.AlreadyCompleted

        party.status shouldBe Party.Status.COMPLETED
    }

    @Test
    fun `capture - player not current`() {
        val area = area()

        val playerQueue = mockk<PlayerQueue>()
        every { playerQueue.currentPlayerId } returns playerId()

        val party = party(
            status = Party.Status.STARTED,
            playerQueue = playerQueue
        )

        party.capture(playerId(), area) shouldBeLeft Party.PlayerNotCurrent

        party.status shouldBe Party.Status.STARTED
    }

    @Test
    fun `capture - dices not rolled`() {
        val area = area()
        val currentPlayerId = playerId()

        val playerQueue = mockk<PlayerQueue>()
        every { playerQueue.currentPlayerId } returns currentPlayerId

        val party = party(
            status = Party.Status.STARTED,
            dicePair = null,
            playerQueue = playerQueue
        )

        party.capture(currentPlayerId, area) shouldBeLeft Party.DicesNotRolled

        party.status shouldBe Party.Status.STARTED
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
            status = Party.Status.STARTED,
            dicePair = dicePair,
            playerQueue = playerQueue
        )

        party.capture(currentPlayerId, area) shouldBeLeft Party.MismatchedArea

        party.status shouldBe Party.Status.STARTED
    }

    @Test
    fun `capture - inaccessible area`() {
        val area = area()
        val field = mockk<Field>()
        val currentPlayerId = playerId()

        val playerQueue = mockk<PlayerQueue>()
        every { playerQueue.currentPlayerId } returns currentPlayerId

        val party = party(
            status = Party.Status.STARTED,
            dicePair = dicePairFor(area),
            field = field,
            playerQueue = playerQueue
        )

        every { field.capture(currentPlayerId, area) } returns Party.InaccessibleArea.left()

        party.capture(currentPlayerId, area) shouldBeLeft Party.InaccessibleArea

        party.status shouldBe Party.Status.STARTED
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
            status = Party.Status.STARTED,
            dicePair = dicePairFor(area),
            field = field,
            playerQueue = playerQueue
        )

        party.capture(currentPlayerId, area) shouldBeRight Unit

        party.status shouldBe Party.Status.STARTED
    }

    private fun dicePairFor(area: Area) =
        mockk<DicePair>().also {
            every { it.isMatched(area) } returns true
        }
}
