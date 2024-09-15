package ru.pavlentygood.cellcapture.domain

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PartyTest {

    @Test
    fun `join player`() {
        val playerId = playerId()
        val name = playerName()
        val party = party(
            playerLimit = 3
        )

        val generatePlayerId = { playerId }

        party.joinPlayer(name, generatePlayerId) shouldBeRight playerId

        party.getPlayers().map { it.id } shouldContain playerId
        party.status shouldBe Party.Status.NEW
    }

    @Test
    fun `join player - limit`() {
        val playerId = playerId()
        val name = playerName()
        val party = party(playerLimit = 1)

        val generatePlayerId = { playerId }

        party.joinPlayer(name, generatePlayerId) shouldBeLeft PlayerCountLimitExceeded

        party.getPlayers() shouldHaveSize 2
        party.status shouldBe Party.Status.NEW
    }

    @Test
    fun `start party`() {
        val party = party()

        party.start(party.ownerId) shouldBeRight Unit

        party.getPlayers() shouldHaveSize 2
        party.status shouldBe Party.Status.STARTED
    }

    @Test
    fun `start party - player not owner when player joined`() {
        val player = player()
        val party = party(
            players = listOf(player)
        )

        party.start(player.id) shouldBeLeft Party.PlayerNotOwner

        party.getPlayers() shouldHaveSize 2
        party.status shouldBe Party.Status.NEW
    }

    @Test
    fun `start party - player not owner when player not joined`() {
        val player = player()
        val party = party(
            players = listOf(player())
        )

        party.start(player.id) shouldBeLeft Party.PlayerNotOwner

        party.getPlayers() shouldHaveSize 2
        party.status shouldBe Party.Status.NEW
    }

    @Test
    fun `start party - too few players`() {
        val party = party(
            players = listOf()
        )

        party.start(party.ownerId) shouldBeLeft Party.TooFewPlayers

        party.getPlayers() shouldHaveSize 1
        party.status shouldBe Party.Status.NEW
    }

    @Test
    fun `start party - already started`() {
        val party = party(
            status = Party.Status.STARTED
        )

        party.start(party.ownerId) shouldBeLeft Party.AlreadyStarted

        party.getPlayers() shouldHaveSize 2
        party.status shouldBe Party.Status.STARTED
    }

    @Test
    fun `start party - already completed`() {
        val party = party(
            status = Party.Status.COMPLETED
        )

        party.start(party.ownerId) shouldBeLeft Party.AlreadyCompleted

        party.getPlayers() shouldHaveSize 2
        party.status shouldBe Party.Status.COMPLETED
    }
}
