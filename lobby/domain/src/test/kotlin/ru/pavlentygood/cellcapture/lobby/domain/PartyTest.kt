package ru.pavlentygood.cellcapture.lobby.domain

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.kernel.domain.playerId
import ru.pavlentygood.cellcapture.kernel.domain.playerName

class PartyTest {

    @Test
    fun `join player`() {
        val playerId = playerId()
        val name = playerName()

        val playerList = mockk<PlayerList>()
        justRun {
            playerList.add(
                match { it.id == playerId && it.name == name }
            )
        }
        every { playerList.players } returns listOf(mockk())

        val party = party(
            playerLimit = 3,
            playerList = playerList
        )

        val generatePlayerId = { playerId }

        party.joinPlayer(name, generatePlayerId) shouldBeRight playerId

        party.started shouldBe false
    }

    @Test
    fun `join player - limit`() {
        val playerId = playerId()
        val name = playerName()

        val playerList = mockk<PlayerList>()
        every { playerList.players } returns listOf(mockk())

        val party = party(
            playerLimit = 1,
            playerList = playerList
        )

        val generatePlayerId = { playerId }

        party.joinPlayer(name, generatePlayerId) shouldBeLeft Party.PlayerCountLimit

        party.started shouldBe false
    }

    @Test
    fun `start party`() {
        val players = listOf(player(), player())

        val playerList = mockk<PlayerList>()
        every { playerList.players } returns players

        val party = party(
            playerList = playerList
        )

        party.start(party.ownerId) shouldBeRight Unit

        party.started shouldBe true
    }

    @Test
    fun `start party - player not owner`() {
        val playerList = mockk<PlayerList>()
        every { playerList.players } returns listOf(mockk(), mockk())

        val party = party(
            playerList = playerList
        )

        party.start(playerId()) shouldBeLeft Party.PlayerNotOwner

        party.started shouldBe false
    }

    @Test
    fun `start party - too few players`() {
        val playerList = mockk<PlayerList>()
        every { playerList.players } returns listOf(mockk())

        val party = party(
            playerList = playerList
        )

        party.start(party.ownerId) shouldBeLeft Party.TooFewPlayers

        party.started shouldBe false
    }

    @Test
    fun `start party - already started`() {
        val playerList = mockk<PlayerList>()
        every { playerList.players } returns listOf(mockk(), mockk())

        val party = party(
            started = true,
            playerList = playerList
        )

        party.start(party.ownerId) shouldBeLeft Party.AlreadyStarted

        party.started shouldBe true
    }
}
